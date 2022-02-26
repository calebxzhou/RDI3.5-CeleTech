package calebzhou.rdimc.celestech.mixin.mobs;

import net.minecraft.entity.mob.AbstractSkeletonEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(AbstractSkeletonEntity.class)
public abstract class MixinHarderSkeleton {
    @ModifyConstant(
            method = "Lnet/minecraft/entity/mob/AbstractSkeletonEntity;createAbstractSkeletonAttributes()Lnet/minecraft/entity/attribute/DefaultAttributeContainer$Builder;",
            constant = @Constant(doubleValue = 0.25D)
    )
    private static double changeSpeed(double spd){
        return 0.4D;
    }
    @ModifyConstant(
            method = "Lnet/minecraft/entity/mob/AbstractSkeletonEntity;updateAttackType()V",
            constant = @Constant(intValue = 20)
    )
    private static int changeAtkSpeed(int spd){
        return 2;
    }
}
