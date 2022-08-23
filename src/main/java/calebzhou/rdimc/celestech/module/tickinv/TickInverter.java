package calebzhou.rdimc.celestech.module.tickinv;

import calebzhou.rdimc.celestech.RDICeleTech;
import calebzhou.rdimc.celestech.ServerStatus;
import calebzhou.rdimc.celestech.utils.ServerUtils;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraft.world.level.block.entity.TickingBlockEntity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;
import java.util.concurrent.Phaser;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

import static calebzhou.rdimc.celestech.ServerStatus.BAD;

public class TickInverter {
    public static final TickInverter INSTANCE= new TickInverter();
    //线程池执行器
    private ExecutorService executor ;
    //线程序号
    private AtomicInteger ThreadID = null;
    //线程池同步用同步屏障
    private Phaser phaser;
    private MinecraftServer mcs;
    // Tick执行中指示器
    private AtomicBoolean isTicking = new AtomicBoolean();
    public void init(){
        ThreadID = new AtomicInteger();
        ForkJoinPool.ForkJoinWorkerThreadFactory poolThreadFactory = p -> {
            ForkJoinWorkerThread poolThread = ForkJoinPool.defaultForkJoinWorkerThreadFactory.newThread(p);
            poolThread.setName("TickInv-" + ThreadID.getAndIncrement());
            poolThread.setContextClassLoader(RDICeleTech.class.getClassLoader());
            return poolThread;
        };
        executor = new ForkJoinPool(Runtime.getRuntime().availableProcessors(), poolThreadFactory, (t, e) -> {
            e.printStackTrace();
        }, true);
    }
    public void beforeGettingAllLevels(int size, MinecraftServer server){
        isTicking.set(true);
        phaser = new Phaser(size + 1);
        mcs = server;
    }
    AtomicInteger currentWorlds = new AtomicInteger();
    public void callTick(ServerLevel serverworld, BooleanSupplier hasTimeLeft, MinecraftServer server) {
        if (mcs != server) {
            System.err.println("tick好几次？");
            serverworld.tick(hasTimeLeft);
            return;
        } else {
            executor.execute(() -> {
                try {
                    currentWorlds.incrementAndGet();
                    serverworld.tick(hasTimeLeft);
                } catch (Throwable e){
                    e.printStackTrace();
                    ServerUtils.broadcastChatMessage("tick world错误"+e+e.getCause());

                } finally {
                    phaser.arriveAndDeregister();
                    currentWorlds.decrementAndGet();
                }
                //TODO bug不少 需要继续改造 e.g.Accessing LegacyRandomSource from multiple threads
            });
        }
    }
    public static class EntityInv implements ITickDelayable{

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
        public int getDelayTickListSize(){
            return delayTickList.size();
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
public static class BlockEntity implements ITickDelayable{
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
    public int getDelayTickListSize(){
        return delayTickList.size();
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
