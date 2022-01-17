package calebzhou.rdimc.celestech.mixin;

import net.minecraft.fluid.LavaFluid;
import net.minecraft.world.WanderingTraderManager;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Random;

@Mixin(LavaFluid.class)
public abstract class LavaAccelerateMixin {
    @Overwrite
    public int getTickRate(WorldView world) {
        return world.getDimension().isUltrawarm() ? 10 : 60;
    }



}
