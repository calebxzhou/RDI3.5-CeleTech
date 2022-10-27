package calebzhou.rdi.core.server.mixin.gameplay;

import calebzhou.rdi.core.server.RdiEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//TODO 玩家事件
@Mixin(ServerPlayer.class)
public class mPlayerEvents {
}
//成功放置方块
@Mixin(ServerPlayerGameMode.class)
class mPlaceBlock {

	@Shadow
	@Final
	protected ServerPlayer player;

	@Inject(at = @At("TAIL"),method = "useItemOn")
    private void rdi_recordPlace(ServerPlayer player, Level world, ItemStack stack, InteractionHand hand, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> info) {
        UseOnContext context  = new UseOnContext(player, hand, hitResult);
        BlockPos blockPos = context.getClickedPos();
        BlockState blockState = player.getLevel().getBlockState(blockPos);
		//记录
		RdiEvents.recordBlock(player.getStringUUID(), Registry.BLOCK.getKey(blockState.getBlock()).toString(),0,world,blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }
}
