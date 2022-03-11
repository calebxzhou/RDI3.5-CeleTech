package calebzhou.rdimc.celestech.module.oceanworld;

import calebzhou.rdimc.celestech.mixin.AccessChunkGeneratorSettings;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.function.ToFloatFunction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Spline;
import net.minecraft.util.math.VerticalSurfaceType;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.source.util.VanillaTerrainParameters;
import net.minecraft.world.biome.source.util.VanillaTerrainParametersCreator;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.chunk.*;
import net.minecraft.world.gen.noise.NoiseParametersKeys;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;
import net.minecraft.world.gen.surfacebuilder.MaterialRules.MaterialCondition;
import net.minecraft.world.gen.surfacebuilder.MaterialRules.MaterialRule;
import net.minecraft.world.gen.surfacebuilder.VanillaSurfaceRules;

import static net.minecraft.world.gen.surfacebuilder.MaterialRules.*;

public class OceanWorld {

    public static GenerationShapeConfig getShapeConfig() {
        return GenerationShapeConfig.create(
                -64,
                384,
                new NoiseSamplingConfig(1.0, 1.0, 80.0, 160.0),
                new SlideConfig(-0.078125, 2, 8),
                new SlideConfig(0.1171875, 3, 0),
                1,
                2,
                false,
                false,
                false,
                createSurfaceParameters());
    }

    public static ChunkGeneratorSettings getGenerationConfig() {
        return AccessChunkGeneratorSettings.callCreate(
                new StructuresConfig(true),
                getShapeConfig(),
                Blocks.STONE.getDefaultState(),
                Blocks.WATER.getDefaultState(),
                createOverworldSurfaceRule(),
                127,
                false,
                true,
                true,
                true,
                true,
                false

        );
    }

    private static final ToFloatFunction<Float> field_35673 = float_ -> float_.floatValue();

    public static VanillaTerrainParameters createSurfaceParameters() {
        ToFloatFunction<Float> toFloatFunction = field_35673;
        ToFloatFunction<Float> toFloatFunction2 = field_35673;
        ToFloatFunction<Float> toFloatFunction3 = field_35673;
        Spline<VanillaTerrainParameters.NoisePoint> spline = createLandSpline(
                -0.15f, 0.0f, 0.0f, 0.1f, 0.0f, -0.03f, false, false, toFloatFunction);
        Spline<VanillaTerrainParameters.NoisePoint> spline2 = createLandSpline(
                -0.1f, 0.03f, 0.1f, 0.1f, 0.01f, -0.03f, false, false, toFloatFunction);
        Spline<VanillaTerrainParameters.NoisePoint> spline3 = createLandSpline(
                -0.1f, 0.03f, 0.1f, 0.7f, 0.01f, -0.03f, true, true, toFloatFunction);
        Spline<VanillaTerrainParameters.NoisePoint> spline4 = createLandSpline(
                -0.05f, 0.03f, 0.1f, 1.0f, 0.01f, 0.01f, true, true, toFloatFunction);
        float f = -0.51f;
        float g = -0.4f;
        float h = 0.1f;
        float i = -0.15f;
        Spline<VanillaTerrainParameters.NoisePoint> spline5 = Spline.builder(VanillaTerrainParameters.LocationFunction.CONTINENTS, toFloatFunction)

                .add(-1.1f, 0.044f, 0.0f)

                .add(-1.02f, -0.2222f, 0.0f)

                .add(-0.51f, -0.2222f, 0.0f)

                .add(-0.44f, -0.12f, 0.0f)

                .add(-0.18f, -0.12f, 0.0f)

                .add(-0.16f, spline, 0.0f)

                .add(-0.15f, spline, 0.0f)

                .add(-0.1f, spline2, 0.0f)

                .add(0.25f, spline3, 0.0f)

                .add(1.0f, spline4, 0.0f).build();
        Spline<VanillaTerrainParameters.NoisePoint> spline6 = Spline.builder(VanillaTerrainParameters.LocationFunction.CONTINENTS, field_35673)

                .add(-0.19f, 3.95f, 0.0f)

                .add(-0.15f, buildErosionFactorSpline(6.25f, true, field_35673), 0.0f)

                .add(-0.1f, buildErosionFactorSpline(5.47f, true, toFloatFunction2), 0.0f)

                .add(0.03f, buildErosionFactorSpline(5.08f, true, toFloatFunction2), 0.0f)

                .add(0.06f, buildErosionFactorSpline(4.69f, false, toFloatFunction2), 0.0f).build();
        float j = 0.65f;
        Spline<VanillaTerrainParameters.NoisePoint> spline7 = Spline.builder(VanillaTerrainParameters.LocationFunction.CONTINENTS, toFloatFunction3)

                .add(-0.11f, 0.0f, 0.0f)

                .add(0.03f, method_38856(1.0f, 0.5f, 0.0f, 0.0f, toFloatFunction3), 0.0f)

                .add(0.65f, method_38856(1.0f, 1.0f, 1.0f, 0.0f, toFloatFunction3), 0.0f).build();
        return new VanillaTerrainParameters(spline5, spline6, spline7);
    }

    private static Spline<VanillaTerrainParameters.NoisePoint> createLandSpline(float f, float g, float h, float i, float j, float k, boolean bl, boolean bl2, ToFloatFunction<Float> toFloatFunction) {
        float l = 0.6f;
        float m = 0.5f;
        float n = 0.5f;
        Spline<VanillaTerrainParameters.NoisePoint> spline = method_38219(MathHelper.lerp(i, 0.6f, 1.5f), bl2, toFloatFunction);
        Spline<VanillaTerrainParameters.NoisePoint> spline2 = method_38219(MathHelper.lerp(i, 0.6f, 1.0f), bl2, toFloatFunction);
        Spline<VanillaTerrainParameters.NoisePoint> spline3 = method_38219(i, bl2, toFloatFunction);
        Spline<VanillaTerrainParameters.NoisePoint> spline4 = createFlatOffsetSpline(f - 0.15f, 0.5f * i,
                MathHelper.lerp(0.5f, 0.5f, 0.5f) * i, 0.5f * i, 0.6f * i, 0.5f, toFloatFunction);
        Spline<VanillaTerrainParameters.NoisePoint> spline5 = createFlatOffsetSpline(f, j * i, g * i, 0.5f * i, 0.6f * i, 0.5f, toFloatFunction);
        Spline<VanillaTerrainParameters.NoisePoint> spline6 = createFlatOffsetSpline(f, j, j, g, h, 0.5f, toFloatFunction);
        Spline<VanillaTerrainParameters.NoisePoint> spline7 = createFlatOffsetSpline(f, j, j, g, h, 0.5f, toFloatFunction);
        Spline<VanillaTerrainParameters.NoisePoint> spline8 = Spline.builder(VanillaTerrainParameters.LocationFunction.RIDGES, toFloatFunction)
                .add(-1.0f, f, 0.0f)
                .add(-0.4f, spline6, 0.0f)
                .add(0.0f, h + 0.07f, 0.0f).build();
        Spline<VanillaTerrainParameters.NoisePoint> spline9 = createFlatOffsetSpline(-0.02f, k, k, g, h, 0.0f, toFloatFunction);
        Spline.Builder<VanillaTerrainParameters.NoisePoint> builder = Spline.builder(VanillaTerrainParameters.LocationFunction.EROSION, toFloatFunction)
                .add(-0.85f, spline, 0.0f)
                .add(-0.7f, spline2, 0.0f)
                .add(-0.4f, spline3, 0.0f)
                .add(-0.35f, spline4, 0.0f)
                .add(-0.1f, spline5, 0.0f)
                .add(0.2f, spline6, 0.0f);
        if (bl) {
            builder
                    .add(0.4f, spline7, 0.0f)
                    .add(0.45f, spline8, 0.0f)
                    .add(0.55f, spline8, 0.0f)
                    .add(0.58f, spline7, 0.0f);
        }
        builder
                .add(0.7f, spline9, 0.0f);
        return builder.build();
    }

    private static Spline<VanillaTerrainParameters.NoisePoint> createFlatOffsetSpline(float f, float g, float h, float i, float j, float k, ToFloatFunction<Float> toFloatFunction) {
        float l = Math.max(0.5f * (g - f), k);
        float m = 5.0f * (h - g);
        return Spline.builder(VanillaTerrainParameters.LocationFunction.RIDGES, toFloatFunction)
                .add(-1.0f, f, l)
                .add(-0.4f, g, Math.min(l, m))
                .add(0.0f, h, m)
                .add(0.4f, i, 2.0f * (i - h))
                .add(1.0f, j, 0.7f * (j - i)).build();
    }

    private static Spline<VanillaTerrainParameters.NoisePoint> method_38219(float f, boolean bl, ToFloatFunction<Float> toFloatFunction) {
        Spline.Builder<VanillaTerrainParameters.NoisePoint> builder = Spline.builder(VanillaTerrainParameters.LocationFunction.RIDGES, toFloatFunction);
        float g = -0.7f;
        float h = -1.0f;
        float i = getOffsetValue(-1.0f, f, -0.7f);
        float j = 1.0f;
        float k = getOffsetValue(1.0f, f, -0.7f);
        float l = method_38217(f);
        float m = -0.65f;
        if (-0.65f < l && l < 1.0f) {
            float n = getOffsetValue(-0.65f, f, -0.7f);
            float o = -0.75f;
            float p = getOffsetValue(-0.75f, f, -0.7f);
            float q = method_38210(i, p, -1.0f, -0.75f);
            builder
                    .add(-1.0f, i, q);
            builder
                    .add(-0.75f, p, 0.0f);
            builder
                    .add(-0.65f, n, 0.0f);
            float r = getOffsetValue(l, f, -0.7f);
            float s = method_38210(r, k, l, 1.0f);
            float t = 0.01f;
            builder
                    .add(l - 0.01f, r, 0.0f);
            builder
                    .add(l, r, s);
            builder
                    .add(1.0f, k, s);
        } else {
            float n = method_38210(i, k, -1.0f, 1.0f);
            if (bl) {
                builder
                        .add(-1.0f, Math.max(0.2f, i), 0.0f);
                builder
                        .add(0.0f, MathHelper.lerp(0.5f, i, k), n);
            } else {
                builder
                        .add(-1.0f, i, n);
            }
            builder
                    .add(1.0f, k, n);
        }
        return builder.build();
    }

    private static float method_38210(float f, float g, float h, float i) {
        return (g - f) / (i - h);
    }

    private static float method_38217(float continentalness) {
        float f = 1.17f;
        float g = 0.46082947f;
        float h = 1.0f - (1.0f - continentalness) * 0.5f;
        float i = 0.5f * (1.0f - continentalness);
        return i / (0.46082947f * h) - 1.17f;
    }

    private static float getOffsetValue(float weirdness, float continentalness, float weirdnessThreshold) {
        float f = 1.17f;
        float g = 0.46082947f;
        float h = 1.0f - (1.0f - continentalness) * 0.5f;
        float i = 0.5f * (1.0f - continentalness);
        float j = (weirdness + 1.17f) * 0.46082947f;
        float k = j * h - i;
        if (weirdness < weirdnessThreshold) {
            return Math.max(k, -0.2222f);
        }
        return Math.max(k, 0.0f);
    }

    private static Spline<VanillaTerrainParameters.NoisePoint> buildErosionFactorSpline(float value, boolean bl, ToFloatFunction<Float> toFloatFunction) {
        Spline<VanillaTerrainParameters.NoisePoint> spline = Spline.builder(VanillaTerrainParameters.LocationFunction.WEIRDNESS, toFloatFunction)
                .add(-0.2f, 6.3f, 0.0f)
                .add(0.2f, value, 0.0f).build();
        Spline.Builder<VanillaTerrainParameters.NoisePoint> builder = Spline.builder(VanillaTerrainParameters.LocationFunction.EROSION, toFloatFunction)
                .add(-0.6f, spline, 0.0f)
                .add(-0.5f, Spline.builder(VanillaTerrainParameters.LocationFunction.WEIRDNESS, toFloatFunction)
                        .add(-0.05f, 6.3f, 0.0f)
                        .add(0.05f, 2.67f, 0.0f).build(), 0.0f)
                .add(-0.35f, spline, 0.0f)
                .add(-0.25f, spline, 0.0f)
                .add(-0.1f, Spline.builder(VanillaTerrainParameters.LocationFunction.WEIRDNESS, toFloatFunction)
                        .add(-0.05f, 2.67f, 0.0f)
                        .add(0.05f, 6.3f, 0.0f).build(), 0.0f)
                .add(0.03f, spline, 0.0f);
        if (bl) {
            Spline<VanillaTerrainParameters.NoisePoint> spline2 = Spline.builder(VanillaTerrainParameters.LocationFunction.WEIRDNESS, toFloatFunction)
                    .add(0.0f, value, 0.0f)
                    .add(0.1f, 0.625f, 0.0f).build();
            Spline<VanillaTerrainParameters.NoisePoint> spline3 = Spline.builder(VanillaTerrainParameters.LocationFunction.RIDGES, toFloatFunction)
                    .add(-0.9f, value, 0.0f)
                    .add(-0.69f, spline2, 0.0f).build();
            builder
                    .add(0.35f, value, 0.0f)
                    .add(0.45f, spline3, 0.0f)
                    .add(0.55f, spline3, 0.0f)
                    .add(0.62f, value, 0.0f);
        } else {
            Spline<VanillaTerrainParameters.NoisePoint> spline2 = Spline.builder(VanillaTerrainParameters.LocationFunction.RIDGES, toFloatFunction)
                    .add(-0.7f, spline, 0.0f)
                    .add(-0.15f, 1.37f, 0.0f).build();
            Spline<VanillaTerrainParameters.NoisePoint> spline3 = Spline.builder(VanillaTerrainParameters.LocationFunction.RIDGES, toFloatFunction)
                    .add(0.45f, spline, 0.0f)
                    .add(0.7f, 1.56f, 0.0f).build();
            builder
                    .add(0.05f, spline3, 0.0f)
                    .add(0.4f, spline3, 0.0f)
                    .add(0.45f, spline2, 0.0f)
                    .add(0.55f, spline2, 0.0f)
                    .add(0.58f, value, 0.0f);
        }
        return builder.build();
    }

    private static Spline<VanillaTerrainParameters.NoisePoint> method_38856(float f, float g, float h, float i, ToFloatFunction<Float> toFloatFunction) {
        float j = -0.5775f;
        Spline<VanillaTerrainParameters.NoisePoint> spline = method_38855(f, h, toFloatFunction);
        Spline<VanillaTerrainParameters.NoisePoint> spline2 = method_38855(g, i, toFloatFunction);
        return Spline.builder(VanillaTerrainParameters.LocationFunction.EROSION, toFloatFunction)
                .add(-1.0f, spline, 0.0f)
                .add(-0.78f, spline2, 0.0f)
                .add(-0.5775f, spline2, 0.0f)
                .add(-0.375f, 0.0f, 0.0f).build();
    }

    private static Spline<VanillaTerrainParameters.NoisePoint> method_38855(float f, float g, ToFloatFunction<Float> toFloatFunction) {
        float h = VanillaTerrainParameters.getNormalizedWeirdness(0.4f);
        float i = VanillaTerrainParameters.getNormalizedWeirdness(0.56666666f);
        float j = (h + i) / 2.0f;
        Spline.Builder<VanillaTerrainParameters.NoisePoint> builder = Spline.builder(VanillaTerrainParameters.LocationFunction.RIDGES, toFloatFunction);
        builder
                .add(h, 0.0f, 0.0f);
        if (g > 0.0f) {
            builder
                    .add(j, method_38857(g, toFloatFunction), 0.0f);
        } else {
            builder
                    .add(j, 0.0f, 0.0f);
        }
        if (f > 0.0f) {
            builder
                    .add(1.0f, method_38857(f, toFloatFunction), 0.0f);
        } else {
            builder
                    .add(1.0f, 0.0f, 0.0f);
        }
        return builder.build();
    }

    private static Spline<VanillaTerrainParameters.NoisePoint> method_38857(float f, ToFloatFunction<Float> toFloatFunction) {
        float g = 0.63f * f;
        float h = 0.3f * f;
        return Spline.builder(VanillaTerrainParameters.LocationFunction.WEIRDNESS, toFloatFunction)
                .add(-0.01f, g, 0.0f)
                .add(0.01f, h, 0.0f).build();
    }

    public static MaterialRule createOverworldSurfaceRule() {
        MaterialCondition materialCondition = aboveY(YOffset.fixed(97), 2);
        MaterialCondition materialCondition2 = aboveY(YOffset.fixed(256), 0);
        MaterialCondition materialCondition3 = aboveYWithStoneDepth(YOffset.fixed(63), -1);
        MaterialCondition materialCondition4 = aboveYWithStoneDepth(YOffset.fixed(74), 1);
        MaterialCondition materialCondition5 = aboveY(YOffset.fixed(62), 0);
        MaterialCondition materialCondition6 = aboveY(YOffset.fixed(63), 0);
        MaterialCondition materialCondition7 = water(-1, 0);
        MaterialCondition materialCondition8 = water(0, 0);
        MaterialCondition materialCondition9 = waterWithStoneDepth(-6, -1);
        MaterialCondition materialCondition10 = hole();
        MaterialCondition materialCondition11 = biome(BiomeKeys.FROZEN_OCEAN, BiomeKeys.DEEP_FROZEN_OCEAN);
        MaterialCondition materialCondition12 = steepSlope();
        MaterialRule materialRule = sequence(condition(materialCondition8, GRASS_BLOCK), DIRT, WATER);


        MaterialRule materialRule2 = sequence(condition(STONE_DEPTH_CEILING, SANDSTONE), SAND, WATER);
        MaterialRule materialRule3 = sequence(condition(STONE_DEPTH_CEILING, STONE), GRAVEL, WATER);
        MaterialCondition materialCondition13 = biome(BiomeKeys.WARM_OCEAN, BiomeKeys.BEACH, BiomeKeys.SNOWY_BEACH);
        MaterialCondition biomeDesert = biome(BiomeKeys.DESERT);
        MaterialRule stoneThings = sequence(
                condition(biome(BiomeKeys.STONY_PEAKS),
                        sequence(condition(
                                        noiseThreshold(NoiseParametersKeys.CALCITE, -0.0125, 0.0125),
                                        CALCITE),
                                STONE)),
                condition(biome(BiomeKeys.STONY_SHORE), sequence(condition(noiseThreshold(NoiseParametersKeys.GRAVEL, -0.05, 0.05), materialRule3), STONE)),
                condition(biome(BiomeKeys.WINDSWEPT_HILLS), condition(surfaceNoiseThreshold(1.0), STONE)),
                condition(materialCondition13, materialRule2), condition(biomeDesert, materialRule2), condition(biome(BiomeKeys.DRIPSTONE_CAVES), STONE)
        );
        MaterialRule snowThings = condition(
                noiseThreshold(NoiseParametersKeys.POWDER_SNOW, 0.45, 0.58), condition(
                        materialCondition8, POWDER_SNOW));
        MaterialRule moreSnowThings = condition(
                noiseThreshold(NoiseParametersKeys.POWDER_SNOW, 0.35, 0.6), condition(
                        materialCondition8, POWDER_SNOW));
        MaterialRule materialRule7 = sequence(condition(
                biome(BiomeKeys.FROZEN_PEAKS), sequence(condition(
                        materialCondition12, PACKED_ICE), condition(
                        noiseThreshold(NoiseParametersKeys.PACKED_ICE, -0.5, 0.2), PACKED_ICE), condition(
                        noiseThreshold(NoiseParametersKeys.ICE, -0.0625, 0.025), ICE), condition(
                        materialCondition8, SNOW_BLOCK))), condition(
                biome(BiomeKeys.SNOWY_SLOPES), sequence(condition(
                        materialCondition12, STONE), snowThings, condition(
                        materialCondition8, SNOW_BLOCK))), condition(
                biome(BiomeKeys.JAGGED_PEAKS), STONE), condition(
                biome(BiomeKeys.GROVE), sequence(snowThings, DIRT)), stoneThings, condition(
                biome(BiomeKeys.WINDSWEPT_SAVANNA), condition(
                        surfaceNoiseThreshold(1.75), STONE)), condition(
                biome(BiomeKeys.WINDSWEPT_GRAVELLY_HILLS), sequence(condition(
                        surfaceNoiseThreshold(2.0), materialRule3), condition(
                        surfaceNoiseThreshold(1.0), STONE), condition(
                        surfaceNoiseThreshold(-1.0), DIRT), materialRule3)), DIRT
        );
        MaterialRule materialRule8 = sequence(condition(
                biome(BiomeKeys.FROZEN_PEAKS), sequence(condition(
                        materialCondition12, PACKED_ICE), condition(
                        noiseThreshold(NoiseParametersKeys.PACKED_ICE, 0.0, 0.2), PACKED_ICE), condition(
                        noiseThreshold(NoiseParametersKeys.ICE, 0.0, 0.025), ICE), condition(
                        materialCondition8, SNOW_BLOCK))), condition(
                biome(BiomeKeys.SNOWY_SLOPES), sequence(condition(
                        materialCondition12, STONE), moreSnowThings, condition(
                        materialCondition8, SNOW_BLOCK))), condition(
                biome(BiomeKeys.JAGGED_PEAKS), sequence(condition(
                        materialCondition12, STONE), condition(
                        materialCondition8, SNOW_BLOCK))), condition(
                biome(BiomeKeys.GROVE), sequence(moreSnowThings, condition(
                        materialCondition8, SNOW_BLOCK))), stoneThings, condition(
                biome(BiomeKeys.WINDSWEPT_SAVANNA), sequence(condition(
                        surfaceNoiseThreshold(1.75), STONE), condition(
                        surfaceNoiseThreshold(-0.5), COARSE_DIRT))), condition(
                biome(BiomeKeys.WINDSWEPT_GRAVELLY_HILLS), sequence(condition(
                        surfaceNoiseThreshold(2.0), materialRule3), condition(
                        surfaceNoiseThreshold(1.0), STONE), condition(
                        surfaceNoiseThreshold(-1.0), materialRule), materialRule3)), condition(
                biome(BiomeKeys.OLD_GROWTH_PINE_TAIGA, BiomeKeys.OLD_GROWTH_SPRUCE_TAIGA), sequence(condition(
                        surfaceNoiseThreshold(1.75), COARSE_DIRT), condition(
                        surfaceNoiseThreshold(-0.95), PODZOL))), condition(
                biome(BiomeKeys.ICE_SPIKES), condition(
                        materialCondition8, SNOW_BLOCK)), condition(
                biome(BiomeKeys.MUSHROOM_FIELDS), MYCELIUM), materialRule);
        MaterialRules.MaterialCondition materialCondition14 = MaterialRules.noiseThreshold(NoiseParametersKeys.SURFACE, -0.55/*0.909*/, -0.5454);

        MaterialCondition materialCondition15 = noiseThreshold(NoiseParametersKeys.SURFACE, -0.55/*-0.909*/, -0.5454);
        MaterialCondition materialCondition16 = noiseThreshold(NoiseParametersKeys.SURFACE, 0.1/*0.1818*/, 0.1818);
        MaterialRules.MaterialRule materialRule9 = MaterialRules.sequence(MaterialRules.condition(MaterialRules.STONE_DEPTH_FLOOR,
                        MaterialRules.sequence(MaterialRules.condition(MaterialRules.biome(BiomeKeys.WOODED_BADLANDS),
                                        MaterialRules.condition(materialCondition,
                                                MaterialRules.sequence(MaterialRules.condition(materialCondition14,
                                                                COARSE_DIRT),
                                                        MaterialRules.condition(materialCondition15,
                                                                COARSE_DIRT),
                                                        MaterialRules.condition(materialCondition16,
                                                                COARSE_DIRT),
                                                        materialRule))),
                                MaterialRules.condition(MaterialRules.biome(BiomeKeys.SWAMP),
                                        MaterialRules.condition(materialCondition5,
                                                MaterialRules.condition(MaterialRules.not(materialCondition6),
                                                        MaterialRules.condition(MaterialRules.noiseThreshold(NoiseParametersKeys.SURFACE_SWAMP,
                                                                        0.0),
                                                                WATER)))))),
                MaterialRules.condition(MaterialRules.biome(BiomeKeys.BADLANDS,
                                BiomeKeys.ERODED_BADLANDS,
                                BiomeKeys.WOODED_BADLANDS),
                        MaterialRules.sequence(MaterialRules.condition(MaterialRules.STONE_DEPTH_FLOOR,
                                        MaterialRules.sequence(MaterialRules.condition(materialCondition2,
                                                        ORANGE_TERRACOTTA),
                                                MaterialRules.condition(materialCondition4,
                                                        MaterialRules.sequence(MaterialRules.condition(materialCondition14,
                                                                        TERRACOTTA),
                                                                MaterialRules.condition(materialCondition15,
                                                                        TERRACOTTA),
                                                                MaterialRules.condition(materialCondition16,
                                                                        TERRACOTTA),
                                                                MaterialRules.terracottaBands())),
                                                MaterialRules.condition(materialCondition7,
                                                        MaterialRules.sequence(MaterialRules.condition(MaterialRules.STONE_DEPTH_CEILING,
                                                                        RED_SANDSTONE),
                                                                RED_SAND)),
                                                MaterialRules.condition(MaterialRules.not(materialCondition10),
                                                        ORANGE_TERRACOTTA),
                                                MaterialRules.condition(materialCondition9,
                                                        WHITE_TERRACOTTA),
                                                materialRule3)),
                                MaterialRules.condition(materialCondition3,
                                        MaterialRules.sequence(MaterialRules.condition(materialCondition6,
                                                        MaterialRules.condition(MaterialRules.not(materialCondition4),
                                                                ORANGE_TERRACOTTA)),
                                                MaterialRules.terracottaBands())),
                                MaterialRules.condition(MaterialRules.STONE_DEPTH_FLOOR_WITH_SURFACE_DEPTH,
                                        MaterialRules.condition(materialCondition9,
                                                WHITE_TERRACOTTA)))),
                MaterialRules.condition(MaterialRules.STONE_DEPTH_FLOOR,
                        MaterialRules.condition(materialCondition7,
                                MaterialRules.sequence(MaterialRules.condition(materialCondition11,
                                                MaterialRules.condition(materialCondition10,
                                                        MaterialRules.sequence(MaterialRules.condition(materialCondition8,
                                                                        AIR),
                                                                MaterialRules.condition(MaterialRules.temperature(),
                                                                        ICE),
                                                                WATER))),
                                        materialRule8))),
                MaterialRules.condition(materialCondition9,
                        MaterialRules.sequence(MaterialRules.condition(MaterialRules.STONE_DEPTH_FLOOR,
                                        MaterialRules.condition(materialCondition11,
                                                MaterialRules.condition(materialCondition10,
                                                        WATER))),
                                MaterialRules.condition(MaterialRules.STONE_DEPTH_FLOOR_WITH_SURFACE_DEPTH,
                                        materialRule7),
                                MaterialRules.condition(materialCondition13,
                                        MaterialRules.condition(MaterialRules.stoneDepth(0,
                                                        true,
                                                        true,
                                                        VerticalSurfaceType.FLOOR),
                                                SANDSTONE)))),
                MaterialRules.condition(MaterialRules.STONE_DEPTH_FLOOR,
                        MaterialRules.sequence(MaterialRules.condition(MaterialRules.biome(BiomeKeys.FROZEN_PEAKS,
                                                BiomeKeys.JAGGED_PEAKS),
                                        STONE),
                                MaterialRules.condition(MaterialRules.biome(BiomeKeys.WARM_OCEAN,
                                                BiomeKeys.LUKEWARM_OCEAN,
                                                BiomeKeys.DEEP_LUKEWARM_OCEAN),
                                        materialRule2),
                                materialRule3)));
        ImmutableList.Builder builder = ImmutableList.builder();
        /*builder
.add(condition(
                verticalGradient("bedrock_floor", YOffset.getBottom(), YOffset.aboveBottom(5)), BEDROCK));*/
        MaterialRule materialRule10 = condition(
                surface(), materialRule9);
        builder
                .add(materialRule10);
        builder
                .add(condition(
                        verticalGradient("deepslate", YOffset.fixed(0), YOffset.fixed(8)), DEEPSLATE));
        return sequence((MaterialRule[]) builder.build().toArray(MaterialRule[]::new));

    }

    private static final MaterialRule AIR = registerBlockRule(Blocks.AIR);
    private static final MaterialRule BEDROCK = registerBlockRule(Blocks.BEDROCK);
    private static final MaterialRule WHITE_TERRACOTTA = registerBlockRule(Blocks.WHITE_TERRACOTTA);
    private static final MaterialRule ORANGE_TERRACOTTA = registerBlockRule(Blocks.ORANGE_TERRACOTTA);
    private static final MaterialRule TERRACOTTA = registerBlockRule(Blocks.TERRACOTTA);
    private static final MaterialRule RED_SAND = registerBlockRule(Blocks.RED_SAND);
    private static final MaterialRule RED_SANDSTONE = registerBlockRule(Blocks.RED_SANDSTONE);
    private static final MaterialRule STONE = registerBlockRule(Blocks.STONE);
    private static final MaterialRule DEEPSLATE = registerBlockRule(Blocks.DEEPSLATE);
    private static final MaterialRule DIRT = registerBlockRule(Blocks.DIRT);
    private static final MaterialRule PODZOL = registerBlockRule(Blocks.PODZOL);
    private static final MaterialRule COARSE_DIRT = registerBlockRule(Blocks.COARSE_DIRT);
    private static final MaterialRule MYCELIUM = registerBlockRule(Blocks.MYCELIUM);
    private static final MaterialRule GRASS_BLOCK = registerBlockRule(Blocks.GRASS_BLOCK);
    private static final MaterialRule CALCITE = registerBlockRule(Blocks.CALCITE);
    private static final MaterialRule GRAVEL = registerBlockRule(Blocks.GRAVEL);
    private static final MaterialRule SAND = registerBlockRule(Blocks.SAND);
    private static final MaterialRule SANDSTONE = registerBlockRule(Blocks.SANDSTONE);
    private static final MaterialRule PACKED_ICE = registerBlockRule(Blocks.PACKED_ICE);
    private static final MaterialRule SNOW_BLOCK = registerBlockRule(Blocks.SNOW_BLOCK);
    private static final MaterialRule POWDER_SNOW = registerBlockRule(Blocks.POWDER_SNOW);
    private static final MaterialRule ICE = registerBlockRule(Blocks.ICE);
    private static final MaterialRule WATER = registerBlockRule(Blocks.WATER);
    private static final MaterialRule LAVA = registerBlockRule(Blocks.LAVA);
    private static final MaterialRule NETHERRACK = registerBlockRule(Blocks.NETHERRACK);
    private static final MaterialRule SOUL_SAND = registerBlockRule(Blocks.SOUL_SAND);
    private static final MaterialRule SOUL_SOIL = registerBlockRule(Blocks.SOUL_SOIL);
    private static final MaterialRule BASALT = registerBlockRule(Blocks.BASALT);
    private static final MaterialRule BLACKSTONE = registerBlockRule(Blocks.BLACKSTONE);
    private static final MaterialRule WARPED_WART_BLOCK = registerBlockRule(Blocks.WARPED_WART_BLOCK);
    private static final MaterialRule WARPED_NYLIUM = registerBlockRule(Blocks.WARPED_NYLIUM);
    private static final MaterialRule NETHER_WART_BLOCK = registerBlockRule(Blocks.NETHER_WART_BLOCK);
    private static final MaterialRule CRIMSON_NYLIUM = registerBlockRule(Blocks.CRIMSON_NYLIUM);
    private static final MaterialRule END_STONE = registerBlockRule(Blocks.END_STONE);

    private static MaterialRule registerBlockRule(Block block) {
        return block(block.getDefaultState());
    }

    private static MaterialCondition surfaceNoiseThreshold(double min) {
        return noiseThreshold(NoiseParametersKeys.SURFACE, min / 8.25, Double.MAX_VALUE);
    }
}
