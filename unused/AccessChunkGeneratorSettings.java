package calebzhou.rdi.celestech.mixin;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseSettings;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.SurfaceRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(NoiseGeneratorSettings.class)
public interface AccessChunkGeneratorSettings {
    @Invoker("<init>")
    static NoiseGeneratorSettings callCreate(StructureSettings structuresConfig,
                                             NoiseSettings generationShapeConfig,
                                             BlockState defaultBlock,
                                             BlockState defaultFluid,
                                             SurfaceRules.RuleSource surfaceRule,
                                             int bedrockCeilingY,
                                             boolean mobGenerationDisabled, boolean aquifers,
                                             boolean noiseCaves, boolean oreVeins,
                                             boolean noodleCaves, boolean useLegacyRandom) {
        return null;
    }

}
