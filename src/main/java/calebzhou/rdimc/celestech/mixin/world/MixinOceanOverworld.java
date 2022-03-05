package calebzhou.rdimc.celestech.mixin.world;

import calebzhou.rdimc.celestech.mixin.AccessDensityFunctions;
import calebzhou.rdimc.celestech.module.oceanworld.OceanWorld;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.GenerationShapeConfig;
import net.minecraft.world.gen.surfacebuilder.VanillaSurfaceRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ChunkGeneratorSettings.class)
public class MixinOceanOverworld {
    /**
     * @author
     */
    @Overwrite
    private static ChunkGeneratorSettings createSurfaceSettings(boolean amplified, boolean largeBiomes) {
        GenerationShapeConfig generationShapeConfig = OceanWorld.getShapeConfig();
        return new ChunkGeneratorSettings(generationShapeConfig,
                Blocks.STONE.getDefaultState(),
                Blocks.WATER.getDefaultState(),
                AccessDensityFunctions.method_41209(generationShapeConfig, false),
                OceanWorld.createOverworldSurfaceRule(),
                127,
                false,
                true,
                true,
                true);
    }
}