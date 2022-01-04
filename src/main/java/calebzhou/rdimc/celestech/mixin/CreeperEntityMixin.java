package calebzhou.rdimc.celestech.mixin;


import net.minecraft.entity.mob.CreeperEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(CreeperEntity.class)
public class CreeperEntityMixin {

    @Shadow @Mutable
    private int fuseTime = 10;
    @Shadow @Mutable
    private int explosionRadius = 6;

    @ModifyConstant(
            method = "Lnet/minecraft/entity/mob/CreeperEntity;initGoals()V",
            constant = @Constant(floatValue = 8.0F)
    )
    private float changeRadius(float constant){
        return 24f;
    }
    @ModifyConstant(
            method = "Lnet/minecraft/entity/mob/CreeperEntity;createCreeperAttributes()Lnet/minecraft/entity/attribute/DefaultAttributeContainer$Builder;",
            constant = @Constant(doubleValue = 0.25D)
    )
    private static double changeSpeed(double spd){
        return 0.55D;
    }
}
