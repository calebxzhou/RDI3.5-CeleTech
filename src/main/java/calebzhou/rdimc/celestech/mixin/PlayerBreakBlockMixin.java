package calebzhou.rdimc.celestech.mixin;

import calebzhou.rdimc.celestech.RDICeleTech;
import calebzhou.rdimc.celestech.event.PlayerBreakBlockCallback;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerInteractionManager.class)
public class PlayerBreakBlockMixin {
	@Shadow protected ServerWorld world;
	@Final @Shadow protected ServerPlayerEntity player;
	@Inject(at = @At("HEAD"), method = "tryBreakBlock(Lnet/minecraft/util/math/BlockPos;)Z")
	private void breakBlock(BlockPos blockPos, CallbackInfoReturnable<Boolean> info) {
		BlockState blockState = world.getBlockState(blockPos);
		ActionResult result = PlayerBreakBlockCallback.EVENT.invoker().interact(player,blockPos,blockState);
		if(result == ActionResult.FAIL)
			info.cancel();
	}
}
