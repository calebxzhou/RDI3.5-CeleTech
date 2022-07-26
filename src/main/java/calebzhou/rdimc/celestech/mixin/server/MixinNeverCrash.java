package calebzhou.rdimc.celestech.mixin.server;

import calebzhou.rdimc.celestech.RDICeleTech;
import calebzhou.rdimc.celestech.module.ticking.TickInverter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.DistanceManager;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.NeighborUpdater;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

import static calebzhou.rdimc.celestech.RDICeleTech.LOGGER;

@Mixin(MinecraftServer.class)
public abstract class MixinNeverCrash {
//用try-catch包起来服务器运行主体，no crash
    @Redirect(method = "runServer()V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;tickServer(Ljava/util/function/BooleanSupplier;)V"))
    private void ticks(MinecraftServer instance, BooleanSupplier shouldKeepTicking){
        try{
            ((MinecraftServer)(Object)this).tickServer(shouldKeepTicking);
        }catch (Throwable e){
            LOGGER.error(e.getMessage());
        }
    }

    private ServerLevel world;
    @Inject(method = "tickChildren(Ljava/util/function/BooleanSupplier;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;tick(Ljava/util/function/BooleanSupplier;)V"),
            locals = LocalCapture.CAPTURE_FAILSOFT)
    private void tickworldInj(BooleanSupplier booleanSupplier, CallbackInfo ci, Iterator var2, ServerLevel serverLevel){
        this.world=serverLevel;
    }
    @Redirect(
            method = "tickChildren(Ljava/util/function/BooleanSupplier;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;tick(Ljava/util/function/BooleanSupplier;)V"))
    private void tickworld(ServerLevel instance, BooleanSupplier shouldKeepTicking){
        try{
            this.world.tick(shouldKeepTicking);
        }catch (Throwable e){
            LOGGER.error(e.getMessage());
        }
    }
}
@Mixin(ServerChunkCache.class)
class AntiChunkCrash{
    @Shadow
    @Final
    private DistanceManager distanceManager;
    @Shadow @Final
    public ChunkMap chunkMap;

    @Redirect(method = "runDistanceManagerUpdates()Z",
            at=@At(value = "INVOKE",target = "Lnet/minecraft/server/level/DistanceManager;runAllUpdates(Lnet/minecraft/server/level/ChunkMap;)Z"))
    private boolean nocrashTick(DistanceManager instance, ChunkMap chunkStorage){
        try{
            boolean tick =  this.distanceManager.runAllUpdates(this.chunkMap);
            return tick;
        }catch (Throwable t){
            LogManager.getLogger().error(t.toString());
        }
        return false;
    }

}
@Mixin(Level.class)
class AntiEntityCrash{
    @Redirect(method = "guardEntityTick(Ljava/util/function/Consumer;Lnet/minecraft/world/entity/Entity;)V",
            at = @At(value = "INVOKE",
                    target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V"))
    private void tickEntity(Consumer tickConsumer, Object entity){
        try {
            TickInverter.INSTANCE.tickEntity(tickConsumer,(Entity) entity);
        } catch (Throwable var6) {
            RDICeleTech.LOGGER.error(var6.getMessage());
        }
    }
}
@Mixin(ServerLevel.class)
abstract class AntiBlockUpdateCrash {
    @Redirect(method = "updateNeighborsAt(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/Block;)V",at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/level/redstone/NeighborUpdater;updateNeighborsAtExceptFromFacing(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/Block;Lnet/minecraft/core/Direction;)V"))
    private void updateNeighborsAt(NeighborUpdater instance, BlockPos blockPos, Block block, Direction direction){
        try {
            ((Level)(Object)this).neighborUpdater.updateNeighborsAtExceptFromFacing(blockPos, block, null);
        } catch (Throwable e) {
            RDICeleTech.LOGGER.error(e.getMessage());
        }
    }
}