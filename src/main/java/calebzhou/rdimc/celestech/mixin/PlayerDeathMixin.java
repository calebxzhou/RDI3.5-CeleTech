package calebzhou.rdimc.celestech.mixin;

import calebzhou.rdimc.celestech.event.PlayerBreakBlockCallback;
import calebzhou.rdimc.celestech.event.PlayerDeathCallback;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerDeathMixin {

    @Inject(at = @At(value = "INVOKE",
            target = "net/minecraft/entity/LivingEntity.onDeath (Lnet/minecraft/entity/damage/DamageSource;)V",shift = At.Shift.AFTER)
            ,method = "onDeath(Lnet/minecraft/entity/damage/DamageSource;)V")
    private void onDeath(DamageSource source,CallbackInfo info){
        ActionResult result = PlayerDeathCallback.EVENT.invoker().call((PlayerEntity)(Object)this, source);
        if(result == ActionResult.FAIL)
            info.cancel();
    }
}
