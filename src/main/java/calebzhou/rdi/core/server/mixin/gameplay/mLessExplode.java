package calebzhou.rdi.core.server.mixin.gameplay;

import calebzhou.rdi.core.server.misc.ServerLaggingStatus;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.block.TntBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Created by calebzhou on 2022-09-25,20:50.
 */

@Mixin(Explosion.class)
public class mLessExplode {
	@Inject(method = "explode",at = @At("HEAD"), cancellable = true)
	public void RDIexplode(CallbackInfo ci) {
		if(ServerLaggingStatus.INSTANCE.isServerLagging())
			ci.cancel();;

	}
}

@Mixin(PrimedTnt.class)
class mLessExplode3{
	@Inject(method = "tick",at=@At("HEAD"), cancellable = true)
	public void RDIExpolode2(CallbackInfo ci){
		if(ServerLaggingStatus.INSTANCE.isServerLagging()){
			((PrimedTnt)(Object)this).discard();
			ci.cancel();
		}
	}
}
@Mixin(TntBlock.class)
class mLessExplode4{
	@Inject(method = "explode(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V",at=@At("HEAD"), cancellable = true)
	private static void RDIExpolode2(CallbackInfo ci){
		if(ServerLaggingStatus.INSTANCE.isServerLagging())
			ci.cancel();
	}
}
