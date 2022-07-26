package calebzhou.rdimc.celestech.mixin.gameplay;


import net.minecraft.world.entity.monster.Creeper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

//苦力怕更容易爆炸
@Mixin(Creeper.class)
public abstract class MixinCreeper {

    @Shadow @Mutable
    private int maxSwell = 20;
    @Shadow @Mutable
    private int explosionRadius = 8;

//加速
    @ModifyConstant(
            method = "Lnet/minecraft/world/entity/monster/Creeper;createAttributes()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;"
            ,constant = @Constant(doubleValue = 0.25D)
    )
    private static double changeSpeed(double spd){
        return 0.4D;
    }
}
