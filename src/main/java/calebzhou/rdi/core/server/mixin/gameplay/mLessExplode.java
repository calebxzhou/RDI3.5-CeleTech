package calebzhou.rdi.core.server.mixin.gameplay;

import calebzhou.rdi.core.server.misc.ServerLaggingStatus;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TntBlock;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Created by calebzhou on 2022-09-25,20:50.
 */

@Mixin(Explosion.class)
public class mLessExplode {
	@Mutable
	@Shadow
	@Final
	private Explosion.BlockInteraction blockInteraction;

	//所有爆炸都不会破坏方块
	@Inject(method = "<init>(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/damagesource/DamageSource;Lnet/minecraft/world/level/ExplosionDamageCalculator;DDDFZLnet/minecraft/world/level/Explosion$BlockInteraction;)V"
			,at=@At("TAIL"))
	private void noExplodeDestroyBlock(Level level, Entity source, DamageSource damageSource, ExplosionDamageCalculator damageCalculator, double toBlowX, double toBlowY, double toBlowZ, float radius, boolean fire, Explosion.BlockInteraction bi, CallbackInfo ci){
		blockInteraction= Explosion.BlockInteraction.NONE;
	}
	@Inject(method = "explode",at = @At("HEAD"), cancellable = true)
	private void RDIexplode(CallbackInfo ci) {
		if(ServerLaggingStatus.INSTANCE.isServerLagging())
			ci.cancel();;

	}
}

@Mixin(PrimedTnt.class)
class mLessExplode3{
	@Inject(method = "tick",at=@At("HEAD"), cancellable = true)
	private void RDIExpolode2(CallbackInfo ci){
		if(ServerLaggingStatus.INSTANCE.isServerLagging()){
			((PrimedTnt)(Object)this).discard();
			ci.cancel();
		}
	}
}/*
@Mixin(TntBlock.class)
class mLessExplode4{
	@Inject(method = "explode(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V",at=@At("HEAD"), cancellable = true)
	private static void RDIExpolode2(CallbackInfo ci){
		if(ServerLaggingStatus.INSTANCE.isServerLagging())
			ci.cancel();
	}
}
*/
