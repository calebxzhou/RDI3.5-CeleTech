package calebzhou.rdimc.celestech.mixin;

import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.entity.projectile.FireballEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//恶魂 增强
@Mixin(GhastEntity.class)
public class GhastEntityMixin {
    @ModifyConstant(
            method = "Lnet/minecraft/entity/mob/GhastEntity;createGhastAttributes()Lnet/minecraft/entity/attribute/DefaultAttributeContainer$Builder;"
            ,constant = @Constant(doubleValue = 10.0D)
    )
    private static double change(double d){
        return 5.0D;
    }
}
@Mixin(GhastEntity.GhastMoveControl.class)
class GhastMoveMixin{
    @ModifyConstant(
            method = "Lnet/minecraft/entity/mob/GhastEntity$GhastMoveControl;tick()V"
            ,constant = @Constant(doubleValue = 0.1D)
    )
    private static double change(double d){
        return 0.2D;
    }
}
@Mixin(GhastEntity.FlyRandomlyGoal.class)
class GhastFlyMixin{
    @ModifyConstant(
            method = "Lnet/minecraft/entity/mob/GhastEntity$FlyRandomlyGoal;start()V"
            ,constant = @Constant(doubleValue = 1.0D)
    )
    private static double change(double d){
        return 1.2D;
    }
}
@Mixin(GhastEntity.ShootFireballGoal.class)
class GhastShootMixin{
    @Shadow @Mutable
    public int cooldown;
    @Shadow @Final
    private GhastEntity ghast;

    @ModifyConstant(
            method = "Lnet/minecraft/entity/mob/GhastEntity$ShootFireballGoal;tick()V"
            ,constant = @Constant(intValue = 20)
    )
    private static int changeCD(int constant){
        return 11;
    }
    @ModifyConstant(
            method = "Lnet/minecraft/entity/mob/GhastEntity$ShootFireballGoal;tick()V"
            ,constant = @Constant(doubleValue = 4.0D)
    )
    private static double changeSped(double constant){
        return 10.0D;
    }

    @Inject(
            method = "Lnet/minecraft/entity/mob/GhastEntity$ShootFireballGoal;tick()V",
            at=@At("TAIL")

    )
    private void changeCd2(CallbackInfo ci){
        if(cooldown<=-40){
            cooldown=9;
            this.ghast.setShooting(true);
        }
    }
}

@Mixin(FireballEntity.class)
class FireballSpeedMixin{
    @Shadow @Mutable private int explosionPower=4;
}