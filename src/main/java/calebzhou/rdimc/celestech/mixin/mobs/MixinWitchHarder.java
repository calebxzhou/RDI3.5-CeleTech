package calebzhou.rdimc.celestech.mixin.mobs;

import net.minecraft.entity.mob.WitchEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(WitchEntity.class)
public class MixinWitchHarder {
    @ModifyConstant(method = "Lnet/minecraft/entity/mob/WitchEntity;createWitchAttributes()Lnet/minecraft/entity/attribute/DefaultAttributeContainer$Builder;",
    constant = @Constant(doubleValue = 0.25D))
    private static double speedUp(double constant){
        return 0.5D;
    }
    @ModifyConstant(method = "Lnet/minecraft/entity/mob/WitchEntity;createWitchAttributes()Lnet/minecraft/entity/attribute/DefaultAttributeContainer$Builder;",
            constant = @Constant(doubleValue = 26.0D))
    private static double healthUp(double constant){
        return 30.0D;
    }
    @ModifyConstant(method = "Lnet/minecraft/entity/mob/WitchEntity;initGoals()V",
            constant = @Constant(intValue = 60))
    private int intervalMinus(int constant){
        return 50;
    }
}
