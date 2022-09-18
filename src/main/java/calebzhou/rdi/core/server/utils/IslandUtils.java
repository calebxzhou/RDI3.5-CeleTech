package calebzhou.rdi.core.server.utils;

import calebzhou.rdi.core.server.RdiCoreServer;
import calebzhou.rdi.core.server.RdiSharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import xyz.nucleoid.fantasy.RuntimeWorldConfig;

public class IslandUtils {
    public static RuntimeWorldConfig getIslandWorldConfig(){
        return new RuntimeWorldConfig()
                .setDimensionType(BuiltinDimensionTypes.OVERWORLD)
                .setDifficulty(Difficulty.HARD)
                .setGameRule(GameRules.RULE_KEEPINVENTORY,true)
                .setGameRule(GameRules.RULE_DAYLIGHT,true)
                .setGameRule(GameRules.RULE_DOFIRETICK,true)
                .setGameRule(GameRules.RULE_DOMOBSPAWNING,true)
                .setGameRule(GameRules.RULE_WEATHER_CYCLE,true)
                .setGenerator(RdiCoreServer.getServer().overworld().getChunkSource().getGenerator())
                .setSeed(System.currentTimeMillis());
    }
    public static ResourceLocation getIslandDimensionLoca(int iid){
        return getIslandDimensionLoca(iid + "");
    }
    public static ResourceLocation getIslandDimensionLoca(String iid){
        return new ResourceLocation(RdiSharedConstants.MOD_ID, RdiSharedConstants.ISLAND_DIMENSION_PREFIX + iid);
    }
	public static void placeInitialBlocks(Level world){
		BlockPos basePos = new BlockPos(0,127,0);
		WorldUtils.placeBlock(world,basePos, Blocks.OBSIDIAN);
		BlockPos chestPos = basePos.offset(0, 1, -1);
		WorldUtils.placeBlock(world, chestPos,Blocks.CHEST.defaultBlockState());
		if (world.getBlockEntity(chestPos) instanceof ChestBlockEntity chestBlockEntity) {
			chestBlockEntity.setItem(1,new ItemStack(Items.BONE,64));
			chestBlockEntity.setItem(2,new ItemStack(Items.JUNGLE_SAPLING,4));
			chestBlockEntity.setItem(3,new ItemStack(Items.LAVA_BUCKET,1));
			chestBlockEntity.setItem(4,new ItemStack(Items.WATER_BUCKET,2));
		}
	}
}
