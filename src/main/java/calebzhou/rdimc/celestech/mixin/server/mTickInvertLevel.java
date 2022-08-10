package calebzhou.rdimc.celestech.mixin.server;

import calebzhou.rdimc.celestech.RDICeleTech;
import calebzhou.rdimc.celestech.module.ticking.TickInverter;
import calebzhou.rdimc.celestech.utils.ServerUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerFunctionManager;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.DistanceManager;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.TickingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.ticks.LevelChunkTicks;
import net.minecraft.world.ticks.LevelTicks;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.Queue;
import java.util.function.BiConsumer;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

@Mixin(Level.class)
public abstract class mTickInvertLevel {

    /*private static final int ENTITY_TICK_LIMIT = 35;

    //最重要的部分，防止机器卡顿
    @Shadow @Final @Mutable protected List<TickingBlockEntity> blockEntityTickers;

    @Shadow public abstract boolean destroyBlock(BlockPos blockPos, boolean bl, @Nullable Entity entity, int i);*/

    @Redirect(method = "tickBlockEntities()V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/entity/TickingBlockEntity;tick()V"))
    private void tickBlockEntity(TickingBlockEntity blockEntityTickInvoker){
        try {
            TickInverter.INSTANCE.tickBlockEntity(blockEntityTickInvoker);
            /*double mspt=ServerUtils.getMillisecondPerTick();
            String name = blockEntityTickInvoker.getName();
            BlockPos bpos = blockEntityTickInvoker.getPos();
            boolean explicit = false;
            if(mspt>100){
                BLOCKENTITY_TICK_LIMIT=10;
                BLOCKENTITY_TICK_FROZEN=80;
                explicit=true;
            }else if(mspt>60){
                BLOCKENTITY_TICK_LIMIT=15;
                BLOCKENTITY_TICK_FROZEN=40;
            }else
            if(mspt>50){
                BLOCKENTITY_TICK_LIMIT=25;
                BLOCKENTITY_TICK_FROZEN=20;
            }
            if(size>4096){
                BLOCKENTITY_TICK_LIMIT=20;
                BLOCKENTITY_TICK_FROZEN=200;
            }
            if(size >1024){
                BLOCKENTITY_TICK_LIMIT=30;
                BLOCKENTITY_TICK_FROZEN=100;
            }

            //冻结时间 ++, 如果被冻结
            if(frozenBlockEntityMap.containsKey(bpos)){
                Integer frozenTime = frozenBlockEntityTimeMap.get(bpos);
                //达到指定时间以后就解冻
                if(frozenTime>=BLOCKENTITY_TICK_FROZEN){
                    frozenBlockEntityTimeMap.remove(bpos);
                    frozenBlockEntityMap.remove(bpos);
                }
                frozenBlockEntityTimeMap.put(bpos,++frozenTime);
            }else{
                if(explicit){
                    //直接删除tick
                     blockEntityTickers.remove(blockEntityTickInvoker);
                    World world = (World) (Object) this;
                    BlockState blockState = world.getBlockState(bpos);
                    world.getChunk(bpos).
                    ItemStack defaultStack = blockState.getBlock().asItem().getDefaultStack();
                    world.setBlockState(bpos, Blocks.AIR.getDefaultState());
                    world.spawnEntity(new ItemEntity(world,bpos.getX(),bpos.getY(),bpos.getZ(),defaultStack));
                   if(RandomUtils.nextInt(0,5)>0)
                        return;
                }

                long t1=System.currentTimeMillis();
                blockEntityTickInvoker.tick();
                long t2=System.currentTimeMillis();

                long dt=t2-t1;
                if(dt > BLOCKENTITY_TICK_LIMIT){
                    frozenBlockEntityMap.put(bpos,name);
                    frozenBlockEntityTimeMap.put(bpos,0);
                }

            }*/
        } catch (Exception e) {
            e.printStackTrace();
            BlockPos pos = blockEntityTickInvoker.getPos();
            ServerUtils.broadcastChatMessage(String.format("RDI TickInverter捕获到异常：坐标%s的block entity出现问题：%s",pos,e.getCause()+e.getMessage()));
        }
    }
    @Redirect(method = "guardEntityTick(Ljava/util/function/Consumer;Lnet/minecraft/world/entity/Entity;)V",
            at = @At(value = "INVOKE",
                    target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V"))
    private void tickEntity(Consumer tickConsumer, Object entity){
        try {
            TickInverter.INSTANCE.tickEntity(tickConsumer,(Entity) entity);
        } catch (Exception e) {
            TickInverter.handleEntityException(e, (Entity) entity,"5");
        }
    }


}
@Mixin(MinecraftServer.class)
abstract
class mTickInvertServer {
    @Shadow public abstract void tickServer(BooleanSupplier booleanSupplier);

    @Shadow public abstract void tickChildren(BooleanSupplier booleanSupplier);

    @Shadow public abstract ServerFunctionManager getFunctions();

    //用try-catch包起来服务器运行主体，防止崩溃
    @Redirect(method = "runServer",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;tickServer(Ljava/util/function/BooleanSupplier;)V"))
    private void tickServerNoCrash(MinecraftServer instance, BooleanSupplier shouldKeepTicking){
        try{
            tickServer(shouldKeepTicking);
        }catch (Throwable e){
            e.printStackTrace();
            ServerUtils.broadcastChatMessage("tick server错误"+ e +e.getCause());
        }
    }
    @Redirect(method = "tickServer",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;tickChildren(Ljava/util/function/BooleanSupplier;)V"))
    private void tickServerChildrenNoCrash(MinecraftServer instance, BooleanSupplier bs){
        try{
            tickChildren(bs);
        }catch (Throwable e){
            e.printStackTrace();
            ServerUtils.broadcastChatMessage("tick server children错误"+e+e.getCause());
        }
    }
    @Redirect(method = "tickChildren",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/ServerFunctionManager;tick()V"))
    private void tickFunctionNoCrash(ServerFunctionManager instance){
        try{
            getFunctions().tick();
        }catch (Throwable e){
            e.printStackTrace();
            ServerUtils.broadcastChatMessage("tick function错误"+e+e.getCause());
        }
    }


    private ServerLevel world;
    @Inject(method = "tickChildren",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;tick(Ljava/util/function/BooleanSupplier;)V"),
            locals = LocalCapture.CAPTURE_FAILSOFT)
    private void tickWorldInjector(BooleanSupplier booleanSupplier, CallbackInfo ci, Iterator var2, ServerLevel serverLevel){
        world=serverLevel;
    }
    @Redirect(
            method = "tickChildren",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;tick(Ljava/util/function/BooleanSupplier;)V"))
    private void tickWorldNoCrash(ServerLevel instance, BooleanSupplier shouldKeepTicking){
        try{
            world.tick(shouldKeepTicking);
        }catch (Throwable e){
            e.printStackTrace();
            ServerUtils.broadcastChatMessage("tick world错误"+e+e.getCause());

        }
    }
}
@Mixin(LevelTicks.class)
abstract
class mTickInvertLevelTicks{
    @Shadow protected abstract void collectTicks(long l, int i, ProfilerFiller profilerFiller);

    @Shadow protected abstract void runCollectedTicks(BiConsumer biConsumer);

    @Shadow protected abstract void cleanupAfterTick();

    @Shadow protected abstract void sortContainersToTick(long l);

    @Shadow protected abstract void drainContainers(long l, int i);

    @Shadow protected abstract void rescheduleLeftoverContainers();

    @Shadow protected abstract void drainFromCurrentContainer(Queue<LevelChunkTicks> queue, LevelChunkTicks levelChunkTicks, long l, int i);

    @Shadow @Final private Queue<LevelChunkTicks> containersToTick;

    @Redirect(method = "tick",at = @At(value = "INVOKE",target = "Lnet/minecraft/world/ticks/LevelTicks;collectTicks(JILnet/minecraft/util/profiling/ProfilerFiller;)V"))
    private void tickCollectTickNoCrash(LevelTicks instance, long l, int i, ProfilerFiller profilerFiller){
        try {
            collectTicks(l, i, profilerFiller);
        } catch (Exception e) {
            e.printStackTrace();
            ServerUtils.broadcastChatMessage("collect tick 错误"+e+e.getCause());
        }
    }
    @Redirect(method = "tick",at = @At(value = "INVOKE",target = "Lnet/minecraft/world/ticks/LevelTicks;runCollectedTicks(Ljava/util/function/BiConsumer;)V"))
    private void runCollectedTickNoCrash(LevelTicks instance, BiConsumer biConsumer){
        try {
            runCollectedTicks(biConsumer);
        } catch (Exception e) {
            e.printStackTrace();
            ServerUtils.broadcastChatMessage("run collected tick 错误"+e+e.getCause());
        }
    }
    @Redirect(method = "tick",at = @At(value = "INVOKE",target = "Lnet/minecraft/world/ticks/LevelTicks;cleanupAfterTick()V"))
    private void cleanUpTickNoCrash(LevelTicks instance){
        try {
            cleanupAfterTick();
        } catch (Exception e) {
            e.printStackTrace();
            ServerUtils.broadcastChatMessage("cleanup tick 错误"+e+e.getCause());
        }
    }
    @Redirect(method = "collectTicks",at = @At(value = "INVOKE",target = "Lnet/minecraft/world/ticks/LevelTicks;sortContainersToTick(J)V"))
    private void sortContainerNoCrash(LevelTicks instance, long l){
        try {
            sortContainersToTick(l);
        } catch (Exception e) {
            e.printStackTrace();
            ServerUtils.broadcastChatMessage("sortContainersToTick错误"+e+e.getCause());
        }
    }
    @Redirect(method = "collectTicks",at = @At(value = "INVOKE",target = "Lnet/minecraft/world/ticks/LevelTicks;drainContainers(JI)V"))
    private void drainContainerNoCrash(LevelTicks instance, long l, int i){
        try {
            drainContainers(l,i);
        } catch (Exception e) {
            e.printStackTrace();
            ServerUtils.broadcastChatMessage("drainContainers错误"+e+e.getCause());
        }
    }
    @Redirect(method = "collectTicks",at = @At(value = "INVOKE",target = "Lnet/minecraft/world/ticks/LevelTicks;rescheduleLeftoverContainers()V"))
    private void rescheduleLeftoverContainersNoCrash(LevelTicks instance, long l){
        try {
            rescheduleLeftoverContainers();
        } catch (Exception e) {
            e.printStackTrace();
            ServerUtils.broadcastChatMessage("rescheduleLeftoverContainers错误"+e+e.getCause());
        }
    }
    @Redirect(method = "drainContainers",at = @At(value = "INVOKE",target = "Lnet/minecraft/world/ticks/LevelTicks;drainFromCurrentContainer(Ljava/util/Queue;Lnet/minecraft/world/ticks/LevelChunkTicks;JI)V"))
    private  void  drainFromCurrentContainerNoCrash(LevelTicks instance, Queue<LevelChunkTicks> queue, LevelChunkTicks levelChunkTicks, long l, int i){
        try {
            drainFromCurrentContainer(containersToTick,levelChunkTicks,l,i);
        } catch (Exception e) {
            e.printStackTrace();
            ServerUtils.broadcastChatMessage("drainFromCurrentContaine错误"+e+e.getCause());

        }
    }
}
@Mixin(ServerLevel.class)
abstract
class mTickInvertServerLevel{
    @Shadow protected abstract void tickPassenger(Entity entity, Entity entity2);

    @Shadow public abstract void tickNonPassenger(Entity entity);

    @Redirect(method = "tickBlock",at = @At(value = "INVOKE",target = "Lnet/minecraft/world/level/block/state/BlockState;tick(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/util/RandomSource;)V"))
    private void tickBlock(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource){
        try {
            blockState.tick((ServerLevel) (Object)this, blockPos, ((Level)(Object)this).random);
        } catch (Exception e) {
            ServerUtils.broadcastChatMessage("在"+blockPos.toShortString()+"tick block错误"+e+e.getCause());
            e.printStackTrace();
        }

    }
    @Redirect(method = "tickFluid",at = @At(value = "INVOKE",target = "Lnet/minecraft/world/level/material/FluidState;tick(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V"))
    private void tickFluid(FluidState fluidState, Level level, BlockPos blockPos){
        try {
            fluidState.tick((ServerLevel) (Object)this, blockPos );
        } catch (Exception e) {
            ServerUtils.broadcastChatMessage("在"+blockPos.toShortString()+"tick fluid错误"+e+e.getCause());
            e.printStackTrace();
        }

    }
    //lambda：entityTickList forEach
    @Redirect(method = "method_31420",at = @At(value = "INVOKE",target = "Lnet/minecraft/server/level/ServerLevel;guardEntityTick(Ljava/util/function/Consumer;Lnet/minecraft/world/entity/Entity;)V"))
    private void tickEntityNoCrash(ServerLevel instance, Consumer tickNonPassenger, Entity entity){
        try {
            ((Level)(Object)this).guardEntityTick(tickNonPassenger, entity);
        }
        catch (Exception e) {
           TickInverter.handleEntityException(e,entity,"3");
        }
    }
    @Redirect(method = "tickNonPassenger",at = @At(value = "INVOKE",target = "Lnet/minecraft/world/entity/Entity;tick()V"))
    private void tickEntity(Entity entity){
        try {
            entity.tick();
        } catch (Exception e) {
            TickInverter.handleEntityException(e,entity,"1");
        }

    }
    @Redirect(method = "tickNonPassenger",at = @At(value = "INVOKE",target = "Lnet/minecraft/server/level/ServerLevel;tickPassenger(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/Entity;)V"))
    private void tickPassengerR(ServerLevel serverLevel, Entity entity, Entity entity2){
        try {
            tickPassenger(entity, entity2);
        } catch (Exception e) {
            TickInverter.handleEntityException(e,entity,"2");
            entity2.discard();
        }

    }

}
@Mixin(ServerChunkCache.class)
class mTickInvertChunk {
    @Shadow
    @Final
    private DistanceManager distanceManager;
    @Shadow @Final
    public ChunkMap chunkMap;

    @Redirect(method = "runDistanceManagerUpdates()Z",
            at=@At(value = "INVOKE",target = "Lnet/minecraft/server/level/DistanceManager;runAllUpdates(Lnet/minecraft/server/level/ChunkMap;)Z"))
    private boolean nocrashTick(DistanceManager instance, ChunkMap chunkStorage){
        try{
            return this.distanceManager.runAllUpdates(this.chunkMap);
        }catch (Exception t){
            t.printStackTrace();
            t=null;
        }
        return false;
    }

}
