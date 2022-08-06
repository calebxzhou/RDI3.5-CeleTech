package calebzhou.rdimc.celestech.mixin.server;

import calebzhou.rdimc.celestech.module.ticking.TickInverter;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.DistanceManager;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
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



@Mixin(Level.class)
class mNoEntityCrash{
    @Redirect(method = "guardEntityTick(Ljava/util/function/Consumer;Lnet/minecraft/world/entity/Entity;)V",
            at = @At(value = "INVOKE",
                    target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V"))
    private void tickEntity(Consumer tickConsumer, Object entity){
        try {
            TickInverter.INSTANCE.tickEntity(tickConsumer,(Entity) entity);
        } catch (Throwable t) {
            t=null;

        }
    }
}
/*
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
}*/
