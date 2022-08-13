package calebzhou.rdimc.celestech.module;

import calebzhou.rdimc.celestech.RDICeleTech;
import calebzhou.rdimc.celestech.ServerStatus;
import calebzhou.rdimc.celestech.utils.ServerUtils;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.level.block.entity.TickingBlockEntity;

import java.util.function.Consumer;

import static calebzhou.rdimc.celestech.ServerStatus.BAD;

public class TickInverter {
    public static final TickInverter INSTANCE= new TickInverter();

    public static void handleEntityException(Exception ex,Entity entity,String msg){
        ServerUtils.broadcastChatMessage("在"+entity.toString()+"tick entity错误！"+ex+"原因："+msg+ex.getCause()+"。已经强制删除！");
        ex.printStackTrace();
        if(!(ex instanceof NullPointerException) ){
            entity.discard();
            entity=null;
        }
    }
    public void tickEntity(Consumer tickConsumer, Entity ent){
        try {
            tickConsumer.accept(ent);
        }  catch(Exception e) {
            TickInverter.handleEntityException(e,ent,"4");
        }

        if(ServerStatus.flag<BAD)
            return;

        boolean remove = true;
        if(ent instanceof Minecart
                || ent instanceof Player
                || ent instanceof Villager
                || ent instanceof Animal
        || ent instanceof ArmorStand
        || ent instanceof Boat){
            remove=false;
        }
        else if(ent instanceof ItemEntity ite){
            if(ite.getItem().hasTag())
                remove=false;
        }
        else if(ent instanceof Mob mobEnt) {
            if(mobEnt.isPersistenceRequired())
                remove=false;
        }else if(Registry.ENTITY_TYPE.getKey(ent.getType()).getNamespace().equals("botania"))
            remove=false;


        if(remove){
            RDICeleTech.LOGGER.info("即将清除：{}",ent.toString());
            ent.remove(Entity.RemovalReason.DISCARDED);
        }

    }

    private Object2IntArrayMap<BlockPos> frozenBlockEntityTimeMap = new Object2IntArrayMap<>();
    private Object2ObjectArrayMap<BlockPos,String> frozenBlockEntityMap = new Object2ObjectArrayMap<>();
    private final int BLOCKENTITY_TICK_FROZEN = 50;//冻结50个tick(2.5s)
    private static int BLOCKENTITY_TICK_LIMIT = 40;//ms 方块实体40ms没tick完就直接冻结
    public void tickBlockEntity(TickingBlockEntity invoker){
        String name = invoker.getType();
        BlockPos bpos = invoker.getPos();
        //冻结时间 ++, 如果被冻结
        if(frozenBlockEntityMap.containsKey(bpos)){
            int frozenTime = frozenBlockEntityTimeMap.getInt(bpos);
            //达到指定时间以后就解冻
            if(frozenTime>=BLOCKENTITY_TICK_FROZEN){
                frozenBlockEntityTimeMap.remove(bpos,frozenTime);
                frozenBlockEntityMap.remove(bpos,name);
            }
            frozenBlockEntityTimeMap.put(bpos,++frozenTime);
        }else{//如果没被冻结
            long t1=System.currentTimeMillis();
            invoker.tick();
            long t2=System.currentTimeMillis();

            if(ServerStatus.flag>=BAD || t2-t1>BLOCKENTITY_TICK_LIMIT){
                frozenBlockEntityMap.put(bpos,name);
                frozenBlockEntityTimeMap.put(bpos,0);
            }
        }
    }

}
