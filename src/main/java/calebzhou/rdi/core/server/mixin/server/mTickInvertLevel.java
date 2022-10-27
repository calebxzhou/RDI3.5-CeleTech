package calebzhou.rdi.core.server.mixin.server;

import calebzhou.rdi.core.server.RdiCoreServer;
import calebzhou.rdi.core.server.command.impl.TpsCommand;
import calebzhou.rdi.core.server.misc.ServerLaggingStatus;
import calebzhou.rdi.core.server.misc.TickTaskManager;
import calebzhou.rdi.core.server.mixin.AccessCollectingNeighborUpdater;
import calebzhou.rdi.core.server.mixin.AccessMultiNeighborUpdate;
import calebzhou.rdi.core.server.module.tickinv.BlockEntityTickInverter;
import calebzhou.rdi.core.server.module.tickinv.EntityTickInverter;
import calebzhou.rdi.core.server.module.tickinv.EntityTicking;
import calebzhou.rdi.core.server.module.tickinv.WorldTickThreadManager;
import calebzhou.rdi.core.server.utils.ServerUtils;
import calebzhou.rdi.core.server.utils.WorldUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
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
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.TickingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.redstone.CollectingNeighborUpdater;
import net.minecraft.world.ticks.LevelChunkTicks;
import net.minecraft.world.ticks.LevelTicks;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.Queue;
import java.util.function.BiConsumer;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

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

    @Shadow public abstract void tickChildren(BooleanSupplier booleanSupplier);

    @Shadow public abstract ServerFunctionManager getFunctions();

	@Shadow
	@Final
	private Map<ResourceKey<Level>, ServerLevel> levels;

	@Shadow
	public abstract ServerLevel overworld();

	@Shadow
	public abstract Iterable<ServerLevel> getAllLevels();

	@Shadow
	private int tickCount;

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

//TODO  关键一步：世界间异步tick
	/*@Inject(method = "tickChildren",at = @At(value = "INVOKE",target = "Lnet/minecraft/server/MinecraftServer;getAllLevels()Ljava/lang/Iterable;"), cancellable = true)
	private void asyncTick(BooleanSupplier hasTimeLeft, CallbackInfo ci){

	}*/
	//private final Iterable<ServerLevel> emptyVanillaTickWorlds = new ArrayList<>();
    //tick之前
   /* @Inject(method = "tickChildren",
    at=@At(value = "INVOKE",target = "Lnet/minecraft/server/MinecraftServer;getAllLevels()Ljava/lang/Iterable;"))
    private void RDIbeforeTick2(BooleanSupplier hasTimeLeft, CallbackInfo ci){
		WorldTickThreadManager.onServerCallWorldTick2(hasTimeLeft);
	}
    @Redirect(method = "tickChildren",
    at=@At(value = "INVOKE",target = "Lnet/minecraft/server/MinecraftServer;getAllLevels()Ljava/lang/Iterable;"))
    private Iterable<ServerLevel> RDIbeforeTick(MinecraftServer instance){
		return emptyVanillaTickWorlds;
	}*/

    @Redirect(
            method = "tickChildren",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;tick(Ljava/util/function/BooleanSupplier;)V"))
    private void tickWorldNoCrash(ServerLevel world, BooleanSupplier shouldKeepTicking){
        WorldTickThreadManager.onServerCallWorldTick(world,shouldKeepTicking);
    }
	/*@Redirect(method = "reloadResources", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;isSameThread()Z"))
	private boolean onServerExecutionThreadPatch(MinecraftServer minecraftServer) {
		return ParallelProcessor.serverExecutionThreadPatch(minecraftServer);
	}
	@Redirect(method = "prepareLevels", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerChunkCache;getTickingGenerated()I"))
	private int initialChunkCountBypass(ServerChunkCache instance) {
		if (DebugHookTerminator.isBypassLoadTarget())
			return 441;
		int loaded = overworld().getChunkSource().getLoadedChunksCount();
		return Math.min(loaded, 441); // Maybe because multi loading caused overflow
	}*/
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
   /* @Redirect(method = "tickNonPassenger",at = @At(value = "INVOKE",target = "Lnet/minecraft/world/entity/Entity;tick()V"))
    private void tickEntity(Entity entity){
        try {
            entity.tick();
        } catch (Exception e) {
            EntityTickInverter.handleEntityException(e,entity,"1");
        }

    }
    @Redirect(method = "tickNonPassenger",at = @At(value = "INVOKE",target = "Lnet/minecraft/server/level/ServerLevel;tickPassenger(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/Entity;)V"))
    private void tickPassengerR(ServerLevel serverLevel, Entity entity, Entity entity2){
        try {
            tickPassenger(entity, entity2);
        } catch (Exception e) {
            EntityTickInverter.handleEntityException(e,entity,"2");
            entity2.discard();
        }

    }*/

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
@Mixin(CollectingNeighborUpdater.class)
abstract
class mTickInvertNeighborUpdater{

	@Shadow
	@Final
	private Level level;

	/*@Redirect(method = "runUpdates", at = @At(value = "INVOKE",target = "Lnet/minecraft/world/level/redstone/CollectingNeighborUpdater$NeighborUpdates;runNext(Lnet/minecraft/world/level/Level;)Z"))
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
	}*/
}
