package calebzhou.rdimc.celestech.mixin.server;

import calebzhou.rdimc.celestech.RDICeleTech;
import calebzhou.rdimc.celestech.module.ticking.TickInverter;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.DistanceManager;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
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
    @Inject(method = "tickServer(Ljava/util/function/BooleanSupplier;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;tickChildren(Ljava/util/function/BooleanSupplier;)V"),
            locals = LocalCapture.CAPTURE_FAILSOFT)
    private void tickworldInj(BooleanSupplier shouldKeepTicking, CallbackInfo ci, Iterator var2,ServerLevel serverWorld){
        this.world=serverWorld;
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
@Mixin(Level.class)
class AntiBlockUpdateCrash{
    @Redirect(method = "neighborChanged(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/Block;Lnet/minecraft/core/BlockPos;)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockState;neighborChanged(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/Block;Lnet/minecraft/core/BlockPos;Z)V"))
    private void updateNeigh(BlockState blockState, Level world, BlockPos pos, Block sourceBlock, BlockPos neighborPos, boolean b){
        try {
            blockState.neighborChanged((Level)((Object) this), pos, sourceBlock, neighborPos, false);
        } catch (Throwable e) {
            RDICeleTech.LOGGER.error(e.getMessage());
        }
    }
}