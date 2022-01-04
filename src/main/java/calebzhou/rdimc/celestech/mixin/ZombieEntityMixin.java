package calebzhou.rdimc.celestech.mixin;

import net.minecraft.entity.mob.ZombieEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ZombieEntity.class)
public class ZombieEntityMixin {
    @ModifyConstant(
            method = "Lnet/minecraft/entity/mob/ZombieEntity;createZombieAttributes()Lnet/minecraft/entity/attribute/DefaultAttributeContainer$Builder;",
            constant = @Constant(doubleValue = 0.23000000417232513D)
    )
    private static double changeSpeed(double spd){
        return 0.40D;
    }

    @ModifyConstant(
            method = "Lnet/minecraft/entity/mob/ZombieEntity;createZombieAttributes()Lnet/minecraft/entity/attribute/DefaultAttributeContainer$Builder;",
            constant = @Constant(doubleValue = 3.0D)
    )
    private static double changeAtk(double spd){
        return 6.0D;
    }
}
