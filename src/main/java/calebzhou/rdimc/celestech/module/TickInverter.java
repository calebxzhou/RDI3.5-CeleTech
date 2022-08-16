package calebzhou.rdimc.celestech.module;

import calebzhou.rdimc.celestech.RDICeleTech;
import calebzhou.rdimc.celestech.ServerStatus;
import calebzhou.rdimc.celestech.utils.ServerUtils;
import calebzhou.rdimc.celestech.utils.ThreadPool;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.level.block.entity.TickingBlockEntity;
import org.apache.http.client.methods.HttpPost;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedTransferQueue;
import java.util.function.Consumer;

import static calebzhou.rdimc.celestech.ServerStatus.BAD;

public class TickInverter {
    public static final TickInverter INSTANCE= new TickInverter();

    public static class EntityInv{
        static class EntityBeingTick{
            public Consumer tickConsumer;
            public Entity entity;
            public String uuid;

            public EntityBeingTick(Consumer tickConsumer, Entity entity) {
                this.tickConsumer = tickConsumer;
                this.entity = entity;
                this.uuid=entity.getStringUUID();
            }
        }
        public static final EntityInv INSTANCE= new EntityInv();
        private final Object2ObjectLinkedOpenHashMap<String,EntityBeingTick> delayTickList = new Object2ObjectLinkedOpenHashMap<>();
        private static final int ENTITY_TICK_LIMIT = 40;//ms 实体40ms没tick完就直接冻结
        public static void handleEntityException(Exception ex,Entity entity,String msg){
            ServerUtils.broadcastChatMessage("在"+entity.toString()+"tick entity错误！"+ex+"原因："+msg+ex.getCause()+"。已经强制删除！");
            ex.printStackTrace();
            if(!(ex instanceof NullPointerException) ){
                entity.discard();
                entity=null;
            }
        }
        public void releaseDelayTickList(){

            if(delayTickList.size()==0)
                return;
            String uid = delayTickList.firstKey();
            if(ServerStatus.flag<BAD){
                    // RDICeleTech.LOGGER.info("entity {} 从延迟tick队列中取出",pollE.entity.toString());
                EntityBeingTick entityBeingTick = delayTickList.get(uid);
                entityBeingTick.tickConsumer.accept(entityBeingTick.entity);
                delayTickList.removeFirst();
            }
        }
        public void tickEntity(Consumer tickConsumer, Entity entity){
            EntityBeingTick invoker = new EntityBeingTick(tickConsumer,entity);
            if(delayTickList.containsKey(entity.getStringUUID())){
                return;
            }
            try {
                tickConsumer.accept(entity);
            } catch (Exception e) {
                handleEntityException(e,entity,"7");
            }




            //tick计时
            //如果服务器延迟高于BAD
            if(ServerStatus.flag>=BAD ){
                delayTickList.put(entity.getStringUUID(),invoker);
            }




            if(ServerStatus.flag>=BAD){
                boolean remove = true;
                if(entity instanceof AbstractMinecart
                        || entity instanceof Player
                        || entity instanceof Villager
                        || entity instanceof Animal
                        || entity instanceof ArmorStand
                        || entity instanceof Boat
                        || entity instanceof ItemFrame
                        || entity instanceof IronGolem
                        || entity instanceof SnowGolem){
                    remove=false;
                }
                else if(entity instanceof ItemEntity ite){
                    if(ite.getItem().hasTag())
                        remove=false;
                }
                else if(entity instanceof Mob mobEnt) {
                    if(mobEnt.isPersistenceRequired())
                        remove=false;
                }else if(Registry.ENTITY_TYPE.getKey(entity.getType()).getNamespace().equals("botania"))
                    remove=false;

                if(remove){
                    RDICeleTech.LOGGER.info("即将清除：{}",entity.toString());
                    entity.remove(Entity.RemovalReason.DISCARDED);
                }
            }


        }
    }


/*
if(ServerStatus.flag<BAD){

                invoker.tick();
                delayTickList.remove(invoker);
            }
 */
public static class BlockEntity  {
    public static final BlockEntity INSTANCE= new BlockEntity();
    private final Object2ObjectLinkedOpenHashMap<BlockPos,TickingBlockEntity> delayTickList = new Object2ObjectLinkedOpenHashMap<>();
    private static final int BLOCKENTITY_TICK_LIMIT = 40;//ms 方块实体40ms没tick完就直接冻结
    public void tickBlockEntity(TickingBlockEntity invoker){
        try {
            BlockPos bpos = invoker.getPos();
            //如果已经有了
            if(delayTickList.containsKey(bpos)){
                return;
            }
            invoker.tick();

            //如果服务器延迟高于BAD
            if(ServerStatus.flag>=BAD){
                delayTickList.put(bpos,invoker);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void releaseDelayTickList(){
        if(delayTickList.size()==0)
            return;
        BlockPos blockPos = delayTickList.firstKey();
            if(ServerStatus.flag<BAD){
                //RDICeleTech.LOGGER.info("blockEntity {} 从延迟tick队列中取出",pollBE.getPos()+"("+pollBE.getType()+")");
                delayTickList.get(blockPos).tick();
                delayTickList.removeFirst();

            }
    }

}



}
