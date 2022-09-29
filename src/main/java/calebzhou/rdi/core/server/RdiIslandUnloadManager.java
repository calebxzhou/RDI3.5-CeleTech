package calebzhou.rdi.core.server;

import calebzhou.rdi.core.server.utils.ServerUtils;
import calebzhou.rdi.core.server.utils.WorldUtils;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.server.level.ServerLevel;
import xyz.nucleoid.fantasy.Fantasy;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by calebzhou on 2022-09-29,21:05.
 */
public class RdiIslandUnloadManager extends TimerTask{
	public static final RdiIslandUnloadManager INSTANCE = new RdiIslandUnloadManager();
	static{
		//每分钟检查一次维度卸载队列
		new Timer().schedule(INSTANCE,0L,60*1000L);
	}
	private static final Object2ObjectOpenHashMap<String,ServerLevel> dimNameLevelMap = new Object2ObjectOpenHashMap<>();
	public static void addIslandToUnloadQueue(ServerLevel level){
		String dimensionName = WorldUtils.getDimensionName(level);
		if(!WorldUtils.isInIsland2(level)) {
			RdiCoreServer.LOGGER.info("{} 不是岛屿维度 不会加入卸载队列", dimensionName);
			return;
		}
		if (isIslandInQueue(level)){
			RdiCoreServer.LOGGER.info("卸载队列里面已经有{}了 不会加入卸载队列", dimensionName);
			return;
		}
		dimNameLevelMap.put(dimensionName,level);
		RdiCoreServer.LOGGER.info("{} 已经添加到维度卸载队列", dimensionName);
	}
	public static boolean isIslandInQueue(ServerLevel level){
		return dimNameLevelMap.containsKey(WorldUtils.getDimensionName(level)) || dimNameLevelMap.containsValue(level);
	}
	public static void removeIslandFromQueue(ServerLevel level){
		removeIslandFromQueue(WorldUtils.getDimensionName(level));
	}
	public static void removeIslandFromQueue(String dimensionName){
		RdiCoreServer.LOGGER.info("{}即将移除岛屿卸载队列", dimensionName);
		dimNameLevelMap.remove(dimensionName);
	}
	private static void processUnloadQueue() {
		if(dimNameLevelMap.size()==0)
			return;
		dimNameLevelMap.object2ObjectEntrySet().parallelStream().forEach(entry->{
			ServerLevel levelToUnload = entry.getValue();
			String dimensionName = entry.getKey();
			if(!WorldUtils.isInIsland2(levelToUnload)) {
				RdiCoreServer.LOGGER.info("{}不是岛屿维度 不卸载", dimensionName);
				removeIslandFromQueue(dimensionName);
				return;
			}

			if(!WorldUtils.isNoPlayersInLevel(levelToUnload)) {
				RdiCoreServer.LOGGER.info("岛屿{}还有玩家 不卸载", dimensionName);
				removeIslandFromQueue(dimensionName);
				return;
			}
			RdiCoreServer.LOGGER.info("岛屿"+ dimensionName +"没有玩家了，即将卸载");
			RdiTickTaskManager.removeDimension(dimensionName);
			ServerUtils.executeOnServerThread(()->{
				levelToUnload.save(null,true,false);
				Fantasy.get(RdiCoreServer.getServer()).unloadWorld(levelToUnload);
			});
			dimNameLevelMap.remove(dimensionName);
		});
		//ServerLevel levelToUnload = levelQueue.poll();

	}
	@Override
	public void run() {
		processUnloadQueue();
	}


}
