package calebzhou.rdi.core.server.mixin.server;

import calebzhou.rdi.core.server.ticking.BlockEntityTickInverter;
import calebzhou.rdi.core.server.ticking.WorldTicking;
import calebzhou.rdi.core.server.utils.ServerUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
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
import net.minecraft.world.level.redstone.CollectingNeighborUpdater;
import net.minecraft.world.ticks.LevelChunkTicks;
import net.minecraft.world.ticks.LevelTicks;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Queue;
import java.util.function.BiConsumer;
import java.util.function.BooleanSupplier;

@Mixin(Level.class)
public abstract class mTickInvertLevel {
    @Redirect(method = "tickBlockEntities()V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/entity/TickingBlockEntity;tick()V"))
    private void tickBlockEntity(TickingBlockEntity invoker){
        BlockEntityTickInverter.handleTick((Level)(Object)this,invoker);
    }

}
@Mixin(MinecraftServer.class)
abstract
class mTickInvertServer {
    @Shadow public abstract void tickServer(BooleanSupplier booleanSupplier);

	@Shadow
	public abstract ServerLevel overworld();

	@Shadow
	@Final
	private List<Runnable> tickables;

	@Shadow
	public abstract void tickChildren(BooleanSupplier hasTimeLeft);


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

	@Inject(method = "tickChildren",at=@At("HEAD"), cancellable = true)
	public void tickChildrenl(BooleanSupplier hasTimeLeft, CallbackInfo ci) {
		WorldTicking.onTick(hasTimeLeft,(MinecraftServer)(Object)this,tickables);
		ci.cancel();
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

}/*
@Mixin(CollectingNeighborUpdater.class)
abstract
class mTickInvertNeighborUpdater{

	@Shadow
	@Final
	private Level level;

	@Redirect(method = "runUpdates", at = @At(value = "INVOKE",target = "Lnet/minecraft/world/level/redstone/CollectingNeighborUpdater$NeighborUpdates;runNext(Lnet/minecraft/world/level/Level;)Z"))
	private boolean RDIrunnext(CollectingNeighborUpdater.NeighborUpdates neighborUpdates, Level level){
		if(ServerLaggingStatus.INSTANCE.isServerLagging()){

			BlockPos updatePos;
			Block sourceBlock;
			if(neighborUpdates instanceof CollectingNeighborUpdater.MultiNeighborUpdate update){
				updatePos = ((AccessMultiNeighborUpdate)(Object)update).getSourcePos();
				sourceBlock = ((AccessMultiNeighborUpdate)(Object)update).getSourceBlock();
			}else if(neighborUpdates instanceof CollectingNeighborUpdater.FullNeighborUpdate update){
				sourceBlock = update.block();
				updatePos = update.pos();
			}else if(neighborUpdates instanceof CollectingNeighborUpdater.SimpleNeighborUpdate update){
				sourceBlock = update.block();
				updatePos = update.pos();
			}else if(neighborUpdates instanceof CollectingNeighborUpdater.ShapeUpdate update){
				sourceBlock = update.state().getBlock();
				updatePos = update.pos();
			}else{
				sourceBlock = Blocks.AIR;
				updatePos = BlockPos.ZERO;
			}
			if(RdiCoreServer.getServer().overworld() != level){
				TpsCommand.delayTickStatus.put(WorldUtils.getDimensionName(level),
						Component.literal("提交延迟tick邻块任务").append(sourceBlock.getName()).append(Component.literal(updatePos.toShortString())).withStyle(ChatFormatting.GOLD)
				);
			}
			TickTaskManager.addDelayTickTask(level,()->{
				((AccessCollectingNeighborUpdater) this).invokeAddAndRun(updatePos,neighborUpdates);//neighborUpdates.runNext(level);
				TpsCommand.delayTickStatus.put(WorldUtils.getDimensionName(level),
						Component.literal("延迟tick邻块").append(sourceBlock.getName()).append(Component.literal(updatePos.toShortString())).withStyle(ChatFormatting.AQUA)
				);
			});
			return false;
		}else{
			return neighborUpdates.runNext(level);
		}
	}
}
*/
