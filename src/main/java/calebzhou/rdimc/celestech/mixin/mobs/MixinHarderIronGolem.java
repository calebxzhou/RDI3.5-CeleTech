package calebzhou.rdimc.celestech.mixin.mobs;

import net.minecraft.entity.passive.IronGolemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(IronGolemEntity.class)
public abstract class MixinHarderIronGolem {
    //铁傀儡增加血量
    @ModifyConstant(
            method= "Lnet/minecraft/entity/passive/IronGolemEntity;createIronGolemAttributes()Lnet/minecraft/entity/attribute/DefaultAttributeContainer$Builder;",
    constant = @Constant(doubleValue = 100.0D))
    private static double modifyIronGolemHealth(double constant){
        return 300.0D;
    }
    @ModifyConstant(
            method= "Lnet/minecraft/entity/passive/IronGolemEntity;createIronGolemAttributes()Lnet/minecraft/entity/attribute/DefaultAttributeContainer$Builder;",
            constant = @Constant(doubleValue = 0.25D))
    private static double modifyIronGolemSpeed(double constant){
        return 0.4D;
    }
}