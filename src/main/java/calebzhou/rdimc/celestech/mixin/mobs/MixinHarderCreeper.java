package calebzhou.rdimc.celestech.mixin.mobs;


import net.minecraft.entity.mob.CreeperEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(CreeperEntity.class)
public abstract class MixinHarderCreeper {

    @Shadow @Mutable
    private int fuseTime = 40;
    @Shadow @Mutable
    private int explosionRadius = 8;

    @ModifyConstant(
            method = "Lnet/minecraft/entity/mob/CreeperEntity;initGoals()V",
            constant = @Constant(floatValue = 8.0F)
    )
    private float changeRadius(float constant){
        return 12f;
    }
    @ModifyConstant(
            method = "Lnet/minecraft/entity/mob/CreeperEntity;createCreeperAttributes()Lnet/minecraft/entity/attribute/DefaultAttributeContainer$Builder;",
            constant = @Constant(doubleValue = 0.25D)
    )
    private static double changeSpeed(double spd){
        return 0.4D;
    }
}
