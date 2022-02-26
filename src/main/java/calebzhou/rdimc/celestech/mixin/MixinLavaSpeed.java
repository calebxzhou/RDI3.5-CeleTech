package calebzhou.rdimc.celestech.mixin;

import net.minecraft.fluid.LavaFluid;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(LavaFluid.class)
public abstract class MixinLavaSpeed {
    @Overwrite
    public int getTickRate(WorldView world) {
        return world.getDimension().isUltrawarm() ? 10 : 60;
    }



}
