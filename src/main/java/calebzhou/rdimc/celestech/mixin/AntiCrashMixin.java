package calebzhou.rdimc.celestech.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.BlockEntityTickInvoker;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Consumer;

@Mixin(World.class)
public class AntiCrashMixin {
    @Shadow @Final protected static Logger LOGGER;

    @Redirect(
            method = "Lnet/minecraft/world/World;tickEntity(Ljava/util/function/Consumer;Lnet/minecraft/entity/Entity;)V",
            at = @At(
                    value = "INVOKE",
            target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V"
    )
    )
    private void tickEntity(Consumer tickConsumer, Object entity){
        try {
            tickConsumer.accept(entity);
        } catch (Throwable var6) {
            LOGGER.error(var6.getMessage());
        }
    }

    @Redirect(
            method = "Lnet/minecraft/world/World;updateNeighbor(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;Lnet/minecraft/util/math/BlockPos;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/BlockState;neighborUpdate(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;Lnet/minecraft/util/math/BlockPos;Z)V"
            )
    )
    private void updateNeigh(BlockState blockState, World world, BlockPos pos, Block sourceBlock, BlockPos neighborPos, boolean b){
        try {
            blockState.neighborUpdate((World)((Object) this), pos, sourceBlock, neighborPos, false);
        } catch (Throwable e) {
            LOGGER.error(e.getMessage());
        }
    }
    @Redirect(
            method = "Lnet/minecraft/world/World;tickBlockEntities()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/chunk/BlockEntityTickInvoker;tick()V"
            )
    )
    private void tickBlockEntity(BlockEntityTickInvoker blockEntityTickInvoker){
        try {
            blockEntityTickInvoker.tick();
        } catch (Throwable e) {
            LOGGER.error(e.getMessage());
        }
    }
}