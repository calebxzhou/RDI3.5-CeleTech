package calebzhou.rdimc.celestech.mixin.gameplay;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//提高弓箭的杀伤力
@Mixin(AbstractArrow.class)
public abstract class
mArrowDamangeUp {
    @Shadow @Mutable
    private int knockback = 1;


    @Inject(
            method = "onHitEntity(Lnet/minecraft/world/phys/EntityHitResult;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/projectile/AbstractArrow;getPierceLevel()B"
            )
    )
    private void inject(EntityHitResult entityHitResult, CallbackInfo ci){
        Entity entity = entityHitResult.getEntity();
        if(entity instanceof Player player){
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,10*20,1));
        }
    }
}
