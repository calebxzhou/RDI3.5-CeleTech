package calebzhou.rdimc.celestech.mixin;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.LlamaSpitEntity;
import net.minecraft.util.hit.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LlamaSpitEntity.class)
public class LlamaSpitEntityMixin {
    @Inject(
            method = "onEntityHit(Lnet/minecraft/util/hit/EntityHitResult;)V",
            at=@At(
                    value = "INVOKE",
                    target = "net/minecraft/entity/damage/DamageSource.mobProjectile (Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/LivingEntity;)Lnet/minecraft/entity/damage/DamageSource;"
            )
    )
    private void plusDamage(EntityHitResult entityHitResult, CallbackInfo ci){
        if(entityHitResult.getEntity() instanceof PlayerEntity player) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA,15*20,2));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON,15*20,2));
        }

    }
}
