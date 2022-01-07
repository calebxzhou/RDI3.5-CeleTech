package calebzhou.rdimc.celestech.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.util.hit.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//提高弓箭的杀伤力
@Mixin(PersistentProjectileEntity.class)
public abstract class ArrowDamageMixin {
    @Shadow @Mutable
    private int punch = 1;


    @Inject(
            method = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;onEntityHit(Lnet/minecraft/util/hit/EntityHitResult;)V",
            at = @At(
                    value = "INVOKE",
                    target = "net/minecraft/entity/projectile/PersistentProjectileEntity.getPierceLevel ()B"
            )
    )
    private void inject(EntityHitResult entityHitResult, CallbackInfo ci){
        Entity entity = entityHitResult.getEntity();
        if(entity instanceof PlayerEntity player){
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS,10*20,1));
        }
    }
}
