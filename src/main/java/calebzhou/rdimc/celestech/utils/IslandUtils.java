package calebzhou.rdimc.celestech.utils;

import calebzhou.rdimc.celestech.RDICeleTech;
import calebzhou.rdimc.celestech.RdiSharedConstants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import xyz.nucleoid.fantasy.RuntimeWorldConfig;

public class IslandUtils {
    public static RuntimeWorldConfig getIslandWorldConfig(){
        return new RuntimeWorldConfig()
                .setDimensionType(BuiltinDimensionTypes.OVERWORLD)
                .setDifficulty(Difficulty.HARD)
                .setGameRule(GameRules.RULE_KEEPINVENTORY,true)
                .setGenerator(RDICeleTech.getServer().overworld().getChunkSource().getGenerator())
                .setSeed(System.currentTimeMillis());
    }
    public static ResourceLocation getIslandDimensionLoca(int iid){
        return getIslandDimensionLoca(iid + "");
    }
    public static ResourceLocation getIslandDimensionLoca(String iid){
        return new ResourceLocation(RdiSharedConstants.MOD_ID, RdiSharedConstants.ISLAND_DIMENSION_PREFIX + iid);
    }
}
