package calebzhou.rdimc.celestech.module.oceanworld;

import calebzhou.rdimc.celestech.mixin.AccessChunkGeneratorSettings;
import com.google.common.collect.ImmutableList;
import net.minecraft.util.CubicSpline;
import net.minecraft.util.Mth;
import net.minecraft.util.ToFloatFunction;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.TerrainShaper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.SurfaceRules.*;
import net.minecraft.world.level.levelgen.placement.CaveSurface;

import static net.minecraft.world.level.levelgen.SurfaceRules.*;

public class OceanWorld {

    public static NoiseSettings getShapeConfig() {
        return NoiseSettings.create(
                -64,
                384,
                new NoiseSamplingSettings(1.0, 1.0, 10.0, 240.0),
                new NoiseSlider(-0.078125, 2, 8),
                new NoiseSlider(0.1171875, 3, 0),
                1,
                2,
                false,
                false,
                false,
                createSurfaceParameters());
    }

    public static NoiseGeneratorSettings getGenerationConfig() {
        return AccessChunkGeneratorSettings.callCreate(
                new StructureSettings(true),
                getShapeConfig(),
                Blocks.STONE.defaultBlockState(),
                Blocks.WATER.defaultBlockState(),
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

    public static TerrainShaper createSurfaceParameters() {
        ToFloatFunction<Float> toFloatFunction = field_35673;
        ToFloatFunction<Float> toFloatFunction2 = field_35673;
        ToFloatFunction<Float> toFloatFunction3 = field_35673;
        CubicSpline<TerrainShaper.Point> spline = createLandSpline(
                -0.15f, 0.0f, 0.0f, 0.1f, 0.0f, -0.03f, false, false, toFloatFunction);
        CubicSpline<TerrainShaper.Point> spline2 = createLandSpline(
                -0.1f, 0.03f, 0.1f, 0.1f, 0.01f, -0.03f, false, false, toFloatFunction);
        CubicSpline<TerrainShaper.Point> spline3 = createLandSpline(
                -0.1f, 0.03f, 0.1f, 0.7f, 0.01f, -0.03f, true, true, toFloatFunction);
        CubicSpline<TerrainShaper.Point> spline4 = createLandSpline(
                -0.05f, 0.03f, 0.1f, 1.0f, 0.01f, 0.01f, true, true, toFloatFunction);
        float f = -0.51f;
        float g = -0.4f;
        float h = 0.1f;
        float i = -0.15f;
        CubicSpline<TerrainShaper.Point> spline5 = CubicSpline.builder(TerrainShaper.Coordinate.CONTINENTS, toFloatFunction)

                .addPoint(-1.1f, 0.044f, 0.0f)

                .addPoint(-1.02f, -0.2222f, 0.0f)

                .addPoint(-0.51f, -0.2222f, 0.0f)

                .addPoint(-0.44f, -0.12f, 0.0f)

                .addPoint(-0.18f, -0.12f, 0.0f)

                .addPoint(-0.16f, spline, 0.0f)

                .addPoint(-0.15f, spline, 0.0f)

                .addPoint(-0.1f, spline2, 0.0f)

                .addPoint(0.25f, spline3, 0.0f)

                .addPoint(1.0f, spline4, 0.0f).build();
        CubicSpline<TerrainShaper.Point> spline6 = CubicSpline.builder(TerrainShaper.Coordinate.CONTINENTS, field_35673)

                .addPoint(-0.19f, 3.95f, 0.0f)

                .addPoint(-0.15f, buildErosionFactorSpline(6.25f, true, field_35673), 0.0f)

                .addPoint(-0.1f, buildErosionFactorSpline(5.47f, true, toFloatFunction2), 0.0f)

                .addPoint(0.03f, buildErosionFactorSpline(5.08f, true, toFloatFunction2), 0.0f)

                .addPoint(0.06f, buildErosionFactorSpline(4.69f, false, toFloatFunction2), 0.0f).build();
        float j = 0.65f;
        CubicSpline<TerrainShaper.Point> spline7 = CubicSpline.builder(TerrainShaper.Coordinate.CONTINENTS, toFloatFunction3)

                .addPoint(-0.11f, 0.0f, 0.0f)

                .addPoint(0.03f, method_38856(1.0f, 0.5f, 0.0f, 0.0f, toFloatFunction3), 0.0f)

                .addPoint(0.65f, method_38856(1.0f, 1.0f, 1.0f, 0.0f, toFloatFunction3), 0.0f).build();
        return new TerrainShaper(spline5, spline6, spline7);
    }

    private static CubicSpline<TerrainShaper.Point> createLandSpline(float f, float g, float h, float i, float j, float k, boolean bl, boolean bl2, ToFloatFunction<Float> toFloatFunction) {
        float l = 0.6f;
        float m = 0.5f;
        float n = 0.5f;
        CubicSpline<TerrainShaper.Point> spline = method_38219(Mth.lerp(i, 0.6f, 1.5f), bl2, toFloatFunction);
        CubicSpline<TerrainShaper.Point> spline2 = method_38219(Mth.lerp(i, 0.6f, 1.0f), bl2, toFloatFunction);
        CubicSpline<TerrainShaper.Point> spline3 = method_38219(i, bl2, toFloatFunction);
        CubicSpline<TerrainShaper.Point> spline4 = createFlatOffsetSpline(f - 0.15f, 0.5f * i,
                Mth.lerp(0.5f, 0.5f, 0.5f) * i, 0.5f * i, 0.6f * i, 0.5f, toFloatFunction);
        CubicSpline<TerrainShaper.Point> spline5 = createFlatOffsetSpline(f, j * i, g * i, 0.5f * i, 0.6f * i, 0.5f, toFloatFunction);
        CubicSpline<TerrainShaper.Point> spline6 = createFlatOffsetSpline(f, j, j, g, h, 0.5f, toFloatFunction);
        CubicSpline<TerrainShaper.Point> spline7 = createFlatOffsetSpline(f, j, j, g, h, 0.5f, toFloatFunction);
        CubicSpline<TerrainShaper.Point> spline8 = CubicSpline.builder(TerrainShaper.Coordinate.RIDGES, toFloatFunction)
                .addPoint(-1.0f, f, 0.0f)
                .addPoint(-0.4f, spline6, 0.0f)
                .addPoint(0.0f, h + 0.07f, 0.0f).build();
        CubicSpline<TerrainShaper.Point> spline9 = createFlatOffsetSpline(-0.02f, k, k, g, h, 0.0f, toFloatFunction);
        CubicSpline.Builder<TerrainShaper.Point> builder = CubicSpline.builder(TerrainShaper.Coordinate.EROSION, toFloatFunction)
                .addPoint(-0.85f, spline, 0.0f)
                .addPoint(-0.7f, spline2, 0.0f)
                .addPoint(-0.4f, spline3, 0.0f)
                .addPoint(-0.35f, spline4, 0.0f)
                .addPoint(-0.1f, spline5, 0.0f)
                .addPoint(0.2f, spline6, 0.0f);
        if (bl) {
            builder
                    .addPoint(0.4f, spline7, 0.0f)
                    .addPoint(0.45f, spline8, 0.0f)
                    .addPoint(0.55f, spline8, 0.0f)
                    .addPoint(0.58f, spline7, 0.0f);
        }
        builder
                .addPoint(0.7f, spline9, 0.0f);
        return builder.build();
    }

    private static CubicSpline<TerrainShaper.Point> createFlatOffsetSpline(float f, float g, float h, float i, float j, float k, ToFloatFunction<Float> toFloatFunction) {
        float l = Math.max(0.5f * (g - f), k);
        float m = 5.0f * (h - g);
        return CubicSpline.builder(TerrainShaper.Coordinate.RIDGES, toFloatFunction)
                .addPoint(-1.0f, f, l)
                .addPoint(-0.4f, g, Math.min(l, m))
                .addPoint(0.0f, h, m)
                .addPoint(0.4f, i, 2.0f * (i - h))
                .addPoint(1.0f, j, 0.7f * (j - i)).build();
    }

    private static CubicSpline<TerrainShaper.Point> method_38219(float f, boolean bl, ToFloatFunction<Float> toFloatFunction) {
        CubicSpline.Builder<TerrainShaper.Point> builder = CubicSpline.builder(TerrainShaper.Coordinate.RIDGES, toFloatFunction);
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
                    .addPoint(-1.0f, i, q);
            builder
                    .addPoint(-0.75f, p, 0.0f);
            builder
                    .addPoint(-0.65f, n, 0.0f);
            float r = getOffsetValue(l, f, -0.7f);
            float s = method_38210(r, k, l, 1.0f);
            float t = 0.01f;
            builder
                    .addPoint(l - 0.01f, r, 0.0f);
            builder
                    .addPoint(l, r, s);
            builder
                    .addPoint(1.0f, k, s);
        } else {
            float n = method_38210(i, k, -1.0f, 1.0f);
            if (bl) {
                builder
                        .addPoint(-1.0f, Math.max(0.2f, i), 0.0f);
                builder
                        .addPoint(0.0f, Mth.lerp(0.5f, i, k), n);
            } else {
                builder
                        .addPoint(-1.0f, i, n);
            }
            builder
                    .addPoint(1.0f, k, n);
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

    private static CubicSpline<TerrainShaper.Point> buildErosionFactorSpline(float value, boolean bl, ToFloatFunction<Float> toFloatFunction) {
        CubicSpline<TerrainShaper.Point> spline = CubicSpline.builder(TerrainShaper.Coordinate.WEIRDNESS, toFloatFunction)
                .addPoint(-0.2f, 6.3f, 0.0f)
                .addPoint(0.2f, value, 0.0f).build();
        CubicSpline.Builder<TerrainShaper.Point> builder = CubicSpline.builder(TerrainShaper.Coordinate.EROSION, toFloatFunction)
                .addPoint(-0.6f, spline, 0.0f)
                .addPoint(-0.5f, CubicSpline.builder(TerrainShaper.Coordinate.WEIRDNESS, toFloatFunction)
                        .addPoint(-0.05f, 6.3f, 0.0f)
                        .addPoint(0.05f, 2.67f, 0.0f).build(), 0.0f)
                .addPoint(-0.35f, spline, 0.0f)
                .addPoint(-0.25f, spline, 0.0f)
                .addPoint(-0.1f, CubicSpline.builder(TerrainShaper.Coordinate.WEIRDNESS, toFloatFunction)
                        .addPoint(-0.05f, 2.67f, 0.0f)
                        .addPoint(0.05f, 6.3f, 0.0f).build(), 0.0f)
                .addPoint(0.03f, spline, 0.0f);
        if (bl) {
            CubicSpline<TerrainShaper.Point> spline2 = CubicSpline.builder(TerrainShaper.Coordinate.WEIRDNESS, toFloatFunction)
                    .addPoint(0.0f, value, 0.0f)
                    .addPoint(0.1f, 0.625f, 0.0f).build();
            CubicSpline<TerrainShaper.Point> spline3 = CubicSpline.builder(TerrainShaper.Coordinate.RIDGES, toFloatFunction)
                    .addPoint(-0.9f, value, 0.0f)
                    .addPoint(-0.69f, spline2, 0.0f).build();
            builder
                    .addPoint(0.35f, value, 0.0f)
                    .addPoint(0.45f, spline3, 0.0f)
                    .addPoint(0.55f, spline3, 0.0f)
                    .addPoint(0.62f, value, 0.0f);
        } else {
            CubicSpline<TerrainShaper.Point> spline2 = CubicSpline.builder(TerrainShaper.Coordinate.RIDGES, toFloatFunction)
                    .addPoint(-0.7f, spline, 0.0f)
                    .addPoint(-0.15f, 1.37f, 0.0f).build();
            CubicSpline<TerrainShaper.Point> spline3 = CubicSpline.builder(TerrainShaper.Coordinate.RIDGES, toFloatFunction)
                    .addPoint(0.45f, spline, 0.0f)
                    .addPoint(0.7f, 1.56f, 0.0f).build();
            builder
                    .addPoint(0.05f, spline3, 0.0f)
                    .addPoint(0.4f, spline3, 0.0f)
                    .addPoint(0.45f, spline2, 0.0f)
                    .addPoint(0.55f, spline2, 0.0f)
                    .addPoint(0.58f, value, 0.0f);
        }
        return builder.build();
    }

    private static CubicSpline<TerrainShaper.Point> method_38856(float f, float g, float h, float i, ToFloatFunction<Float> toFloatFunction) {
        float j = -0.5775f;
        CubicSpline<TerrainShaper.Point> spline = method_38855(f, h, toFloatFunction);
        CubicSpline<TerrainShaper.Point> spline2 = method_38855(g, i, toFloatFunction);
        return CubicSpline.builder(TerrainShaper.Coordinate.EROSION, toFloatFunction)
                .addPoint(-1.0f, spline, 0.0f)
                .addPoint(-0.78f, spline2, 0.0f)
                .addPoint(-0.5775f, spline2, 0.0f)
                .addPoint(-0.375f, 0.0f, 0.0f).build();
    }

    private static CubicSpline<TerrainShaper.Point> method_38855(float f, float g, ToFloatFunction<Float> toFloatFunction) {
        float h = TerrainShaper.peaksAndValleys(0.4f);
        float i = TerrainShaper.peaksAndValleys(0.56666666f);
        float j = (h + i) / 2.0f;
        CubicSpline.Builder<TerrainShaper.Point> builder = CubicSpline.builder(TerrainShaper.Coordinate.RIDGES, toFloatFunction);
        builder
                .addPoint(h, 0.0f, 0.0f);
        if (g > 0.0f) {
            builder
                    .addPoint(j, method_38857(g, toFloatFunction), 0.0f);
        } else {
            builder
                    .addPoint(j, 0.0f, 0.0f);
        }
        if (f > 0.0f) {
            builder
                    .addPoint(1.0f, method_38857(f, toFloatFunction), 0.0f);
        } else {
            builder
                    .addPoint(1.0f, 0.0f, 0.0f);
        }
        return builder.build();
    }

    private static CubicSpline<TerrainShaper.Point> method_38857(float f, ToFloatFunction<Float> toFloatFunction) {
        float g = 0.63f * f;
        float h = 0.3f * f;
        return CubicSpline.builder(TerrainShaper.Coordinate.WEIRDNESS, toFloatFunction)
                .addPoint(-0.01f, g, 0.0f)
                .addPoint(0.01f, h, 0.0f).build();
    }

    public static RuleSource createOverworldSurfaceRule() {
        ConditionSource materialCondition = yBlockCheck(VerticalAnchor.absolute(97), 2);
        ConditionSource materialCondition2 = yBlockCheck(VerticalAnchor.absolute(256), 0);
        ConditionSource materialCondition3 = yStartCheck(VerticalAnchor.absolute(63), -1);
        ConditionSource materialCondition4 = yStartCheck(VerticalAnchor.absolute(64), 1);
        ConditionSource materialCondition5 = yBlockCheck(VerticalAnchor.absolute(62), 0);
        ConditionSource materialCondition6 = yBlockCheck(VerticalAnchor.absolute(63), 0);
        ConditionSource materialCondition7 = waterBlockCheck(-1, 0);
        ConditionSource materialCondition8 = waterBlockCheck(0, 0);
        ConditionSource materialCondition9 = waterStartCheck(-6, -1);
        ConditionSource materialCondition10 = hole();
        ConditionSource materialCondition11 = isBiome(Biomes.FROZEN_OCEAN, Biomes.DEEP_FROZEN_OCEAN);
        ConditionSource materialCondition12 = steep();
        RuleSource materialRule = sequence(ifTrue(materialCondition8, GRASS_BLOCK), DIRT, WATER);


        RuleSource materialRule2 = sequence(ifTrue(ON_CEILING, SANDSTONE), SAND, WATER);
        RuleSource materialRule3 = sequence(ifTrue(ON_CEILING, STONE), GRAVEL, WATER);
        ConditionSource materialCondition13 = isBiome(Biomes.WARM_OCEAN, Biomes.BEACH, Biomes.SNOWY_BEACH);
        ConditionSource biomeDesert = isBiome(Biomes.DESERT);
        RuleSource stoneThings = sequence(
                ifTrue(isBiome(Biomes.STONY_PEAKS),
                        sequence(ifTrue(
                                        noiseCondition(Noises.CALCITE, -0.0125, 0.0125),
                                        CALCITE),
                                STONE)),
                ifTrue(isBiome(Biomes.STONY_SHORE), sequence(ifTrue(noiseCondition(Noises.GRAVEL, -0.05, 0.05), materialRule3), STONE)),
                ifTrue(isBiome(Biomes.WINDSWEPT_HILLS), ifTrue(surfaceNoiseThreshold(1.0), STONE)),
                ifTrue(materialCondition13, materialRule2), ifTrue(biomeDesert, materialRule2), ifTrue(isBiome(Biomes.DRIPSTONE_CAVES), STONE)
        );
        RuleSource snowThings = ifTrue(
                noiseCondition(Noises.POWDER_SNOW, 0.45, 0.58), ifTrue(
                        materialCondition8, POWDER_SNOW));
        RuleSource moreSnowThings = ifTrue(
                noiseCondition(Noises.POWDER_SNOW, 0.35, 0.6), ifTrue(
                        materialCondition8, POWDER_SNOW));
        RuleSource materialRule7 = sequence(ifTrue(
                isBiome(Biomes.FROZEN_PEAKS), sequence(ifTrue(
                        materialCondition12, PACKED_ICE), ifTrue(
                        noiseCondition(Noises.PACKED_ICE, -0.5, 0.2), PACKED_ICE), ifTrue(
                        noiseCondition(Noises.ICE, -0.0625, 0.025), ICE), ifTrue(
                        materialCondition8, SNOW_BLOCK))), ifTrue(
                isBiome(Biomes.SNOWY_SLOPES), sequence(ifTrue(
                        materialCondition12, STONE), snowThings, ifTrue(
                        materialCondition8, SNOW_BLOCK))), ifTrue(
                isBiome(Biomes.JAGGED_PEAKS), STONE), ifTrue(
                isBiome(Biomes.GROVE), sequence(snowThings, DIRT)), stoneThings, ifTrue(
                isBiome(Biomes.WINDSWEPT_SAVANNA), ifTrue(
                        surfaceNoiseThreshold(1.75), STONE)), ifTrue(
                isBiome(Biomes.WINDSWEPT_GRAVELLY_HILLS), sequence(ifTrue(
                        surfaceNoiseThreshold(2.0), materialRule3), ifTrue(
                        surfaceNoiseThreshold(1.0), STONE), ifTrue(
                        surfaceNoiseThreshold(-1.0), DIRT), materialRule3)), DIRT
        );
        RuleSource materialRule8 = sequence(ifTrue(
                isBiome(Biomes.FROZEN_PEAKS), sequence(ifTrue(
                        materialCondition12, PACKED_ICE), ifTrue(
                        noiseCondition(Noises.PACKED_ICE, 0.0, 0.2), PACKED_ICE), ifTrue(
                        noiseCondition(Noises.ICE, 0.0, 0.025), ICE), ifTrue(
                        materialCondition8, SNOW_BLOCK))), ifTrue(
                isBiome(Biomes.SNOWY_SLOPES), sequence(ifTrue(
                        materialCondition12, STONE), moreSnowThings, ifTrue(
                        materialCondition8, SNOW_BLOCK))), ifTrue(
                isBiome(Biomes.JAGGED_PEAKS), sequence(ifTrue(
                        materialCondition12, STONE), ifTrue(
                        materialCondition8, SNOW_BLOCK))), ifTrue(
                isBiome(Biomes.GROVE), sequence(moreSnowThings, ifTrue(
                        materialCondition8, SNOW_BLOCK))), stoneThings, ifTrue(
                isBiome(Biomes.WINDSWEPT_SAVANNA), sequence(ifTrue(
                        surfaceNoiseThreshold(1.75), STONE), ifTrue(
                        surfaceNoiseThreshold(-0.5), COARSE_DIRT))), ifTrue(
                isBiome(Biomes.WINDSWEPT_GRAVELLY_HILLS), sequence(ifTrue(
                        surfaceNoiseThreshold(2.0), materialRule3), ifTrue(
                        surfaceNoiseThreshold(1.0), STONE), ifTrue(
                        surfaceNoiseThreshold(-1.0), materialRule), materialRule3)), ifTrue(
                isBiome(Biomes.OLD_GROWTH_PINE_TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA), sequence(ifTrue(
                        surfaceNoiseThreshold(1.75), COARSE_DIRT), ifTrue(
                        surfaceNoiseThreshold(-0.95), PODZOL))), ifTrue(
                isBiome(Biomes.ICE_SPIKES), ifTrue(
                        materialCondition8, SNOW_BLOCK)), ifTrue(
                isBiome(Biomes.MUSHROOM_FIELDS), MYCELIUM), materialRule);
        SurfaceRules.ConditionSource materialCondition14 = SurfaceRules.noiseCondition(Noises.SURFACE, -0.55/*0.909*/, -0.5454);

        ConditionSource materialCondition15 = noiseCondition(Noises.SURFACE, -0.55/*-0.909*/, -0.5454);
        ConditionSource materialCondition16 = noiseCondition(Noises.SURFACE, 0.1/*0.1818*/, 0.1818);
        SurfaceRules.RuleSource materialRule9 = SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR,
                        SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.WOODED_BADLANDS),
                                        SurfaceRules.ifTrue(materialCondition,
                                                SurfaceRules.sequence(SurfaceRules.ifTrue(materialCondition14,
                                                                COARSE_DIRT),
                                                        SurfaceRules.ifTrue(materialCondition15,
                                                                COARSE_DIRT),
                                                        SurfaceRules.ifTrue(materialCondition16,
                                                                COARSE_DIRT),
                                                        materialRule))),
                                SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.SWAMP),
                                        SurfaceRules.ifTrue(materialCondition5,
                                                SurfaceRules.ifTrue(SurfaceRules.not(materialCondition6),
                                                        SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.SWAMP,
                                                                        0.0),
                                                                WATER)))))),
                SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.BADLANDS,
                                Biomes.ERODED_BADLANDS,
                                Biomes.WOODED_BADLANDS),
                        SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR,
                                        SurfaceRules.sequence(SurfaceRules.ifTrue(materialCondition2,
                                                        ORANGE_TERRACOTTA),
                                                SurfaceRules.ifTrue(materialCondition4,
                                                        SurfaceRules.sequence(SurfaceRules.ifTrue(materialCondition14,
                                                                        TERRACOTTA),
                                                                SurfaceRules.ifTrue(materialCondition15,
                                                                        TERRACOTTA),
                                                                SurfaceRules.ifTrue(materialCondition16,
                                                                        TERRACOTTA),
                                                                SurfaceRules.bandlands())),
                                                SurfaceRules.ifTrue(materialCondition7,
                                                        SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.ON_CEILING,
                                                                        RED_SANDSTONE),
                                                                RED_SAND)),
                                                SurfaceRules.ifTrue(SurfaceRules.not(materialCondition10),
                                                        ORANGE_TERRACOTTA),
                                                SurfaceRules.ifTrue(materialCondition9,
                                                        WHITE_TERRACOTTA),
                                                materialRule3)),
                                SurfaceRules.ifTrue(materialCondition3,
                                        SurfaceRules.sequence(SurfaceRules.ifTrue(materialCondition6,
                                                        SurfaceRules.ifTrue(SurfaceRules.not(materialCondition4),
                                                                ORANGE_TERRACOTTA)),
                                                SurfaceRules.bandlands())),
                                SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR,
                                        SurfaceRules.ifTrue(materialCondition9,
                                                WHITE_TERRACOTTA)))),
                SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR,
                        SurfaceRules.ifTrue(materialCondition7,
                                SurfaceRules.sequence(SurfaceRules.ifTrue(materialCondition11,
                                                SurfaceRules.ifTrue(materialCondition10,
                                                        SurfaceRules.sequence(SurfaceRules.ifTrue(materialCondition8,
                                                                        AIR),
                                                                SurfaceRules.ifTrue(SurfaceRules.temperature(),
                                                                        ICE),
                                                                WATER))),
                                        materialRule8))),
                SurfaceRules.ifTrue(materialCondition9,
                        SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR,
                                        SurfaceRules.ifTrue(materialCondition11,
                                                SurfaceRules.ifTrue(materialCondition10,
                                                        WATER))),
                                SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR,
                                        materialRule7),
                                SurfaceRules.ifTrue(materialCondition13,
                                        SurfaceRules.ifTrue(SurfaceRules.stoneDepthCheck(0,
                                                        true,
                                                        true,
                                                        CaveSurface.FLOOR),
                                                SANDSTONE)))),
                SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR,
                        SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.FROZEN_PEAKS,
                                                Biomes.JAGGED_PEAKS),
                                        STONE),
                                SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.WARM_OCEAN,
                                                Biomes.LUKEWARM_OCEAN,
                                                Biomes.DEEP_LUKEWARM_OCEAN),
                                        materialRule2),
                                materialRule3)));
        ImmutableList.Builder builder = ImmutableList.builder();
        /*builder
.add(condition(
                verticalGradient("bedrock_floor", YOffset.getBottom(), YOffset.aboveBottom(5)), BEDROCK));*/
        RuleSource materialRule10 = ifTrue(
                abovePreliminarySurface(), materialRule9);
        builder
                .add(materialRule10);
        builder
                .add(ifTrue(
                        verticalGradient("deepslate", VerticalAnchor.absolute(0), VerticalAnchor.absolute(8)), DEEPSLATE));
        return sequence((RuleSource[]) builder.build().toArray(RuleSource[]::new));

    }

    private static final RuleSource AIR = registerBlockRule(Blocks.AIR);
    private static final RuleSource BEDROCK = registerBlockRule(Blocks.BEDROCK);
    private static final RuleSource WHITE_TERRACOTTA = registerBlockRule(Blocks.WHITE_TERRACOTTA);
    private static final RuleSource ORANGE_TERRACOTTA = registerBlockRule(Blocks.ORANGE_TERRACOTTA);
    private static final RuleSource TERRACOTTA = registerBlockRule(Blocks.TERRACOTTA);
    private static final RuleSource RED_SAND = registerBlockRule(Blocks.RED_SAND);
    private static final RuleSource RED_SANDSTONE = registerBlockRule(Blocks.RED_SANDSTONE);
    private static final RuleSource STONE = registerBlockRule(Blocks.STONE);
    private static final RuleSource DEEPSLATE = registerBlockRule(Blocks.DEEPSLATE);
    private static final RuleSource DIRT = registerBlockRule(Blocks.DIRT);
    private static final RuleSource PODZOL = registerBlockRule(Blocks.PODZOL);
    private static final RuleSource COARSE_DIRT = registerBlockRule(Blocks.COARSE_DIRT);
    private static final RuleSource MYCELIUM = registerBlockRule(Blocks.MYCELIUM);
    private static final RuleSource GRASS_BLOCK = registerBlockRule(Blocks.GRASS_BLOCK);
    private static final RuleSource CALCITE = registerBlockRule(Blocks.CALCITE);
    private static final RuleSource GRAVEL = registerBlockRule(Blocks.GRAVEL);
    private static final RuleSource SAND = registerBlockRule(Blocks.SAND);
    private static final RuleSource SANDSTONE = registerBlockRule(Blocks.SANDSTONE);
    private static final RuleSource PACKED_ICE = registerBlockRule(Blocks.PACKED_ICE);
    private static final RuleSource SNOW_BLOCK = registerBlockRule(Blocks.SNOW_BLOCK);
    private static final RuleSource POWDER_SNOW = registerBlockRule(Blocks.POWDER_SNOW);
    private static final RuleSource ICE = registerBlockRule(Blocks.ICE);
    private static final RuleSource WATER = registerBlockRule(Blocks.WATER);
    private static final RuleSource LAVA = registerBlockRule(Blocks.LAVA);
    private static final RuleSource NETHERRACK = registerBlockRule(Blocks.NETHERRACK);
    private static final RuleSource SOUL_SAND = registerBlockRule(Blocks.SOUL_SAND);
    private static final RuleSource SOUL_SOIL = registerBlockRule(Blocks.SOUL_SOIL);
    private static final RuleSource BASALT = registerBlockRule(Blocks.BASALT);
    private static final RuleSource BLACKSTONE = registerBlockRule(Blocks.BLACKSTONE);
    private static final RuleSource WARPED_WART_BLOCK = registerBlockRule(Blocks.WARPED_WART_BLOCK);
    private static final RuleSource WARPED_NYLIUM = registerBlockRule(Blocks.WARPED_NYLIUM);
    private static final RuleSource NETHER_WART_BLOCK = registerBlockRule(Blocks.NETHER_WART_BLOCK);
    private static final RuleSource CRIMSON_NYLIUM = registerBlockRule(Blocks.CRIMSON_NYLIUM);
    private static final RuleSource END_STONE = registerBlockRule(Blocks.END_STONE);

    private static RuleSource registerBlockRule(Block block) {
        return state(block.defaultBlockState());
    }

    private static ConditionSource surfaceNoiseThreshold(double min) {
        return noiseCondition(Noises.SURFACE, min / 8.25, Double.MAX_VALUE);
    }
}
