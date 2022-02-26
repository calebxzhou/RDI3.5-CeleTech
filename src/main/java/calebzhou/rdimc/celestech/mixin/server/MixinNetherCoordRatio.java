package calebzhou.rdimc.celestech.mixin.server;

import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(DimensionType.class)
public class MixinNetherCoordRatio {
    //1:8变成8:1
    @ModifyConstant(
            method = "<clinit>()V",
            constant = @Constant(doubleValue = 8.0)
    )
    private static double asd(double constant){
        return 0.125;
    }

}
