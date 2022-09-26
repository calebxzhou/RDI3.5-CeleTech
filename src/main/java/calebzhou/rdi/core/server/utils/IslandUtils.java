package calebzhou.rdi.core.server.utils;

import calebzhou.rdi.core.server.RdiCoreServer;
import calebzhou.rdi.core.server.RdiSharedConstants;
import calebzhou.rdi.core.server.RdiTickTaskManager;
import calebzhou.rdi.core.server.model.Island2;
import calebzhou.rdi.core.server.model.ResultData;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import xyz.nucleoid.fantasy.Fantasy;
import xyz.nucleoid.fantasy.RuntimeWorldConfig;

public class IslandUtils {
	public static ResultData<Island2> getIslandByPlayer(Player player){
		return RdiHttpClient.sendRequest(Island2.class,"get", "/v37/island2/"+player.getStringUUID());
	}
	public static void unloadIsland(ServerLevel islandLevel,ServerPlayer player){
		//如果在二岛
		if(WorldUtils.isInIsland2(islandLevel)){
			String dimensionName = WorldUtils.getDimensionName(islandLevel);
			RdiCoreServer.LOGGER.info("玩家{}离开了岛屿维度{}",player.getScoreboardName(), dimensionName);
			//如果岛上没有人（除了自己） 就卸载存档
			if(WorldUtils.isNoPlayersInLevel(player,islandLevel)){
				RdiCoreServer.LOGGER.info("岛屿"+ dimensionName +"没有玩家了，即将卸载");
				RdiTickTaskManager.removeDimension(dimensionName);
				ServerUtils.executeOnServerThread(()->{
					islandLevel.save(null,true,false);
					Fantasy.get(RdiCoreServer.getServer()).unloadWorld(islandLevel);
				});
			}
		}
	}
	public static void unloadIsland(ServerPlayer player){
		ServerLevel islandLevel = player.getLevel();
		unloadIsland(islandLevel,player);
	}
    public static RuntimeWorldConfig getIslandWorldConfig(){
        return new RuntimeWorldConfig()
                .setDimensionType(BuiltinDimensionTypes.OVERWORLD)
                .setDifficulty(Difficulty.HARD)
				.setShouldTickTime(true)
				/*.setRaining(true)
				.setThundering(true)*/
				.setTimeOfDay(0L)
				/*.setThundering(30)
				.setRaining(30)*/
				//.setSunny(6000)
                .setGameRule(GameRules.RULE_KEEPINVENTORY,true)
                .setGameRule(GameRules.RULE_DAYLIGHT,true)
                .setGameRule(GameRules.RULE_DOFIRETICK,true)
                .setGameRule(GameRules.RULE_DOMOBSPAWNING,true)
                .setGameRule(GameRules.RULE_WEATHER_CYCLE,true)
				.setGameRule(GameRules.RULE_RANDOMTICKING,30)
                .setGenerator(RdiCoreServer.getServer().overworld().getChunkSource().getGenerator())
                .setSeed(-1145141919810L);
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
			chestBlockEntity.setItem(0,new ItemStack(Items.DIRT,16));
			chestBlockEntity.setItem(1,new ItemStack(Items.BONE,64));
			chestBlockEntity.setItem(2,new ItemStack(Items.JUNGLE_SAPLING,4));
			chestBlockEntity.setItem(3,new ItemStack(Items.LAVA_BUCKET,1));
			chestBlockEntity.setItem(4,new ItemStack(Items.WATER_BUCKET,2));
			chestBlockEntity.setItem(5,new ItemStack(Items.COW_SPAWN_EGG,1));
			chestBlockEntity.setItem(6,new ItemStack(Items.SHEEP_SPAWN_EGG,1));
		}
	}
}
