package calebzhou.rdimc.celestech.mixin;

import calebzhou.rdimc.celestech.utils.ServerCache;
import calebzhou.rdimc.celestech.utils.TextUtils;
import calebzhou.rdimc.celestech.utils.WorldUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.LavaFluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FluidBlock.class)
public class WaterLavaStone2Mixin {
    @Inject(
            method = "Lnet/minecraft/block/FluidBlock;receiveNeighborFluids(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)Z",
            at = @At(value = "INVOKE",target = "Lnet/minecraft/fluid/FluidState;isStill()Z" ,
                    shift = At.Shift.AFTER)
    )
    private void genCobbleStoneAfter(World world, BlockPos pos, BlockState state, CallbackInfoReturnable<Boolean> cir){
        //如果水把岩浆冲灭了
        if (world.getFluidState(pos).isStill()) {
            PlayerEntity player = WorldUtils.getNearestPlayer(world,pos);
            TextUtils.sendChatMessage(player,"您可能不小心把岩浆冲灭了, 可以使用/island melt指令来熔化黑曜石");
            return;
        }
        //如果生成石头
        ServerCache.lavaGenStoneMap.put(pos,state);
    }
}