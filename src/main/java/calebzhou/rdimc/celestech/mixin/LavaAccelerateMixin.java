package calebzhou.rdimc.celestech.mixin;

import net.minecraft.fluid.LavaFluid;
import net.minecraft.world.WanderingTraderManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Random;

@Mixin(LavaFluid.class)
public class LavaAccelerateMixin {
    @ModifyConstant(method = "Lnet/minecraft/fluid/LavaFluid;getTickRate(Lnet/minecraft/world/WorldView;)I",
    constant = @Constant(intValue = 10))
    private int modifyConstSpawnDelay(int constant){
        return 30;
    }



}
