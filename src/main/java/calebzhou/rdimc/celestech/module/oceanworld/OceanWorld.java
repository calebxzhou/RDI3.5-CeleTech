package calebzhou.rdimc.celestech.module.oceanworld;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.source.util.VanillaTerrainParametersCreator;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.chunk.GenerationShapeConfig;
import net.minecraft.world.gen.chunk.NoiseSamplingConfig;
import net.minecraft.world.gen.chunk.SlideConfig;
import net.minecraft.world.gen.noise.NoiseParametersKeys;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;
import net.minecraft.world.gen.surfacebuilder.VanillaSurfaceRules;

import static net.minecraft.world.gen.surfacebuilder.MaterialRules.*;

public class OceanWorld {
    public static GenerationShapeConfig getShapeConfig() {
        return GenerationShapeConfig.create(-64, 384,
                new NoiseSamplingConfig(1.0, 1.0, 80.0, 160.0),
                new SlideConfig(-0.078125, 2, 8),
                new SlideConfig(0.1171875, 3, 0),
                1,
                2,
                VanillaTerrainParametersCreator.createSurfaceParameters(false));

    }

    public static MaterialRule createOverworldSurfaceRule() {
        MaterialCondition materialCondition = aboveY(YOffset.fixed(97), 2);
        MaterialCondition materialCondition2 = aboveY(YOffset.fixed(256), 0);
        MaterialCondition materialCondition3 = aboveYWithStoneDepth(YOffset.fixed(63), -1);
        MaterialCondition materialCondition4 = aboveYWithStoneDepth(YOffset.fixed(74), 1);
        MaterialCondition materialCondition5 = aboveY(YOffset.fixed(62), 0);
        MaterialCondition materialCondition6 = aboveY(YOffset.fixed(63), 0);
        MaterialCondition materialCondition7 = water(-1, 0);
        MaterialCondition water00 = water(0, 0);
        MaterialCondition waterStone = waterWithStoneDepth(-6, -1);
        MaterialCondition hole = hole();
        MaterialCondition biomeFrozenOcean = biome(BiomeKeys.FROZEN_OCEAN, BiomeKeys.DEEP_FROZEN_OCEAN);
        MaterialCondition steepSlope = steepSlope();
        MaterialRule grassDirt = sequence(condition(water00, GRASS_BLOCK), DIRT,

                WATER);
        MaterialRule sand = sequence(condition(STONE_DEPTH_CEILING, SANDSTONE), SAND);
        MaterialRule stoneGravel = sequence(condition(STONE_DEPTH_CEILING, STONE), GRAVEL);
        MaterialCondition biomeBeach = biome(BiomeKeys.WARM_OCEAN, BiomeKeys.BEACH, BiomeKeys.SNOWY_BEACH);
        MaterialCondition biomeDesert = biome(BiomeKeys.DESERT);
        MaterialRule stoneThings = sequence(
                condition(biome(BiomeKeys.STONY_PEAKS),
                        sequence(condition(
                                noiseThreshold(NoiseParametersKeys.CALCITE, -0.0125, 0.0125),
                                CALCITE),
                                STONE)),
                condition(biome(BiomeKeys.STONY_SHORE), sequence(condition(noiseThreshold(NoiseParametersKeys.GRAVEL, -0.05, 0.05), stoneGravel), STONE)),
                condition(biome(BiomeKeys.WINDSWEPT_HILLS), condition(surfaceNoiseThreshold(1.0), STONE)), condition(biomeBeach, sand), condition(biomeDesert, sand), condition(biome(BiomeKeys.DRIPSTONE_CAVES), STONE)
        );
        MaterialRule snowThings = condition(
                noiseThreshold(NoiseParametersKeys.POWDER_SNOW, 0.45, 0.58), condition(
                        water00, POWDER_SNOW));
        MaterialRule moreSnowThings = condition(
                noiseThreshold(NoiseParametersKeys.POWDER_SNOW, 0.35, 0.6), condition(
                        water00, POWDER_SNOW));
        MaterialRule materialRule7 = sequence(condition(
                biome(BiomeKeys.FROZEN_PEAKS), sequence(condition(
                        steepSlope, PACKED_ICE), condition(
                        noiseThreshold(NoiseParametersKeys.PACKED_ICE, -0.5, 0.2), PACKED_ICE), condition(
                        noiseThreshold(NoiseParametersKeys.ICE, -0.0625, 0.025), ICE), condition(
                        water00, SNOW_BLOCK))), condition(
                biome(BiomeKeys.SNOWY_SLOPES), sequence(condition(
                        steepSlope, STONE), snowThings, condition(
                        water00, SNOW_BLOCK))), condition(
                biome(BiomeKeys.JAGGED_PEAKS), STONE), condition(
                biome(BiomeKeys.GROVE), sequence(snowThings, DIRT)), stoneThings, condition(
                biome(BiomeKeys.WINDSWEPT_SAVANNA), condition(
                        surfaceNoiseThreshold(1.75), STONE)), condition(
                biome(BiomeKeys.WINDSWEPT_GRAVELLY_HILLS), sequence(condition(
                        surfaceNoiseThreshold(2.0), stoneGravel), condition(
                        surfaceNoiseThreshold(1.0), STONE), condition(
                        surfaceNoiseThreshold(-1.0), DIRT), stoneGravel)), DIRT
        );
        MaterialRule materialRule8 = sequence(condition(
                biome(BiomeKeys.FROZEN_PEAKS), sequence(condition(
                        steepSlope, PACKED_ICE), condition(
                        noiseThreshold(NoiseParametersKeys.PACKED_ICE, 0.0, 0.2), PACKED_ICE), condition(
                        noiseThreshold(NoiseParametersKeys.ICE, 0.0, 0.025), ICE), condition(
                        water00, SNOW_BLOCK))), condition(
                biome(BiomeKeys.SNOWY_SLOPES), sequence(condition(
                        steepSlope, STONE), moreSnowThings, condition(
                        water00, SNOW_BLOCK))), condition(
                biome(BiomeKeys.JAGGED_PEAKS), sequence(condition(
                        steepSlope, STONE), condition(
                        water00, SNOW_BLOCK))), condition(
                biome(BiomeKeys.GROVE), sequence(moreSnowThings, condition(
                        water00, SNOW_BLOCK))), stoneThings, condition(
                biome(BiomeKeys.WINDSWEPT_SAVANNA), sequence(condition(
                        surfaceNoiseThreshold(1.75), STONE), condition(
                        surfaceNoiseThreshold(-0.5), COARSE_DIRT))), condition(
                biome(BiomeKeys.WINDSWEPT_GRAVELLY_HILLS), sequence(condition(
                        surfaceNoiseThreshold(2.0), stoneGravel), condition(
                        surfaceNoiseThreshold(1.0), STONE), condition(
                        surfaceNoiseThreshold(-1.0), grassDirt), stoneGravel)), condition(
                biome(BiomeKeys.OLD_GROWTH_PINE_TAIGA, BiomeKeys.OLD_GROWTH_SPRUCE_TAIGA), sequence(condition(
                        surfaceNoiseThreshold(1.75), COARSE_DIRT), condition(
                        surfaceNoiseThreshold(-0.95), PODZOL))), condition(
                biome(BiomeKeys.ICE_SPIKES), condition(
                        water00, SNOW_BLOCK)), condition(
                biome(BiomeKeys.MUSHROOM_FIELDS), MYCELIUM), grassDirt);
        MaterialCondition materialCondition15 = noiseThreshold(NoiseParametersKeys.SURFACE, -0.6/*-0.909*/, -0.5454);
        MaterialCondition materialCondition16 = noiseThreshold(NoiseParametersKeys.SURFACE, -0.1818, 0.1818);
        MaterialCondition materialCondition17 = noiseThreshold(NoiseParametersKeys.SURFACE, 0.8/*0.5454*/, 0.909);
        MaterialRule materialRule9 = sequence(condition(
                STONE_DEPTH_FLOOR, sequence(condition(
                        biome(BiomeKeys.WOODED_BADLANDS), condition(
                                materialCondition, sequence(condition(
                                        materialCondition15, COARSE_DIRT), condition(
                                        materialCondition16, COARSE_DIRT), condition(
                                        materialCondition17, COARSE_DIRT), grassDirt))), condition(
                        biome(BiomeKeys.SWAMP), condition(
                                materialCondition5, condition(
                                        not(materialCondition6), condition(
                                                noiseThreshold(NoiseParametersKeys.SURFACE_SWAMP, 0.0), WATER)))))), condition(
                biome(BiomeKeys.BADLANDS, BiomeKeys.ERODED_BADLANDS, BiomeKeys.WOODED_BADLANDS), sequence(condition(
                        STONE_DEPTH_FLOOR, sequence(condition(
                                materialCondition2, ORANGE_TERRACOTTA), condition(
                                materialCondition4, sequence(condition(
                                        materialCondition15, TERRACOTTA), condition(
                                        materialCondition16, TERRACOTTA), condition(
                                        materialCondition17, TERRACOTTA), terracottaBands())), condition(
                                materialCondition7, sequence(condition(
                                        STONE_DEPTH_CEILING, RED_SANDSTONE), RED_SAND)), condition(
                                not(hole), ORANGE_TERRACOTTA), condition(
                                waterStone, WHITE_TERRACOTTA), stoneGravel)), condition(
                        materialCondition3, sequence(condition(
                                materialCondition6, condition(
                                        not(materialCondition4), ORANGE_TERRACOTTA)), terracottaBands())), condition(
                        STONE_DEPTH_FLOOR_WITH_SURFACE_DEPTH, condition(
                                waterStone, WHITE_TERRACOTTA)))), condition(
                STONE_DEPTH_FLOOR, condition(
                        materialCondition7, sequence(condition(
                                biomeFrozenOcean, condition(
                                        hole, sequence(condition(
                                                water00, AIR), condition(
                                                temperature(), ICE), WATER))), materialRule8))), condition(
                waterStone, sequence(condition(
                        STONE_DEPTH_FLOOR, condition(
                                biomeFrozenOcean, condition(
                                        hole, WATER))), condition(
                        STONE_DEPTH_FLOOR_WITH_SURFACE_DEPTH, materialRule7), condition(
                        biomeBeach, condition(
                                STONE_DEPTH_FLOOR_WITH_SURFACE_DEPTH_RANGE_6, SANDSTONE)), condition(
                        biomeDesert, condition(
                                STONE_DEPTH_FLOOR_WITH_SURFACE_DEPTH_RANGE_30, SANDSTONE)))), condition(
                STONE_DEPTH_FLOOR, sequence(condition(
                        biome(BiomeKeys.FROZEN_PEAKS, BiomeKeys.JAGGED_PEAKS), STONE), condition(
                        biome(BiomeKeys.WARM_OCEAN, BiomeKeys.LUKEWARM_OCEAN, BiomeKeys.DEEP_LUKEWARM_OCEAN), sand), stoneGravel)));
        ImmutableList.Builder builder = ImmutableList.builder();
        /*builder.add(condition(
                verticalGradient("bedrock_floor", YOffset.getBottom(), YOffset.aboveBottom(5)), BEDROCK));*/
        MaterialRule materialRule10 = condition(
                surface(), materialRule9);
        builder.add(materialRule10);
        builder.add(condition(
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
