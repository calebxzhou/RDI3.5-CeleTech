package calebzhou.rdimc.celestech.mixin;

import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.utils.TextUtils;
import calebzhou.rdimc.celestech.utils.WorldUtils;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.AreaHelper;
import org.lwjgl.system.CallbackI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractFireBlock.class)
public class CreatePortalMixin {
    @Inject(
            method = "Lnet/minecraft/block/AbstractFireBlock;onBlockAdded(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Z)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/dimension/AreaHelper;getNewPortal(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction$Axis;)Ljava/util/Optional;"
            ),cancellable = true
    )
    private void createPortal(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify, CallbackInfo ci){
        PlayerEntity player = WorldUtils.getNearestPlayer(world,pos);
        if(player==null)
            return;
        /*if(player.experienceLevel<10) {
            TextUtils.sendChatMessage(player,"激活此传送门需要10经验!", MessageType.ERROR);
            ci.cancel();
            return;
        }
        player.experienceLevel-=10;
        TextUtils.sendChatMessage(player,"您成功激活了传送门!",MessageType.SUCCESS);*/
    }
}
