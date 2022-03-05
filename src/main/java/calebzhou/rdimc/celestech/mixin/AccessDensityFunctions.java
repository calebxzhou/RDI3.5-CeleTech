package calebzhou.rdimc.celestech.mixin;

import net.minecraft.world.gen.chunk.GenerationShapeConfig;
import net.minecraft.world.gen.densityfunction.DensityFunctions;
import net.minecraft.world.gen.noise.SimpleNoiseRouter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(DensityFunctions.class)
public interface AccessDensityFunctions {
    @Invoker("method_41209")
    static SimpleNoiseRouter method_41209(GenerationShapeConfig generationShapeConfig, boolean bl){return  null;}
}
