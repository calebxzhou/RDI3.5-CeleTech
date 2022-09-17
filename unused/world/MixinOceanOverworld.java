package calebzhou.rdi.celestech.mixin.world;

import calebzhou.rdi.celestech.module.oceanworld.OceanWorld;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(NoiseGeneratorSettings.class)
public class MixinOceanOverworld {
    /**
     * @author
     */
    @Overwrite
    private static NoiseGeneratorSettings overworld(boolean amplified, boolean largeBiomes) {
        NoiseSettings generationShapeConfig = OceanWorld.getShapeConfig();
        return OceanWorld.getGenerationConfig();
    }
    /*private static ChunkGeneratorSettings createSurfaceSettings(boolean amplified, boolean largeBiomes) {
        return new ChunkGeneratorSettings(
                new StructuresConfig(true),
                GenerationShapeConfig.create(-64, 384,
                        new NoiseSamplingConfig(1.0D, 1.0D, 80.0D, 160.0D),
                        new SlideConfig(-0.078125D, 2, amplified ? 0 : 8),
                        new SlideConfig(amplified ? 0.4D : 0.1171875D, 3, 0),
                        1,
                        2,
                        false,
                        amplified,
                        largeBiomes,
                        VanillaTerrainParametersCreator.createSurfaceParameters(amplified)),
                Blocks.STONE.getDefaultState(),
                Blocks.WATER.getDefaultState(),
                VanillaSurfaceRules.createOverworldSurfaceRule(),
                63,
                false,
                true,
                true,
                true,
                true,
                false);
    }*/
}
