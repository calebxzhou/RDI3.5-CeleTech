package calebzhou.rdimc.celestech.mixin.player;

import calebzhou.rdimc.celestech.RDICeleTech;
import calebzhou.rdimc.celestech.event.PlayerBreakBlockCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerGameMode.class)
public abstract class PlayerBreakBlockMixin {
	@Shadow protected ServerLevel level;
	@Final @Shadow protected ServerPlayer player;
	@Inject(at = @At("HEAD"), method = "Lnet/minecraft/server/level/ServerPlayerGameMode;destroyBlock(Lnet/minecraft/core/BlockPos;)Z")
	private void breakBlock(BlockPos blockPos, CallbackInfoReturnable<Boolean> info) {
		BlockState blockState = level.getBlockState(blockPos);
		InteractionResult result = PlayerBreakBlockCallback.EVENT.invoker().interact(player,blockPos,blockState);
		if(result == InteractionResult.FAIL)
			info.cancel();
	}
}
