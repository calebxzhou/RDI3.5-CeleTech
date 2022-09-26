package calebzhou.rdi.core.server;

import calebzhou.rdi.core.server.utils.ServerUtils;
import calebzhou.rdi.core.server.utils.WorldUtils;
import com.google.common.collect.EvictingQueue;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.world.level.Level;

/**
 * Created by calebzhou on 2022-09-26,8:21.
 */
@SuppressWarnings("ALL")
public class RdiTickTaskManager {
	private static final int queueSize = 204800;
	//维度名vsTick队列
	private static final Object2ObjectOpenHashMap<String, EvictingQueue<Runnable>> dimensionTickQueueMap = new Object2ObjectOpenHashMap<>();
	public static void addDelayTickTask(Level level, Runnable tickableRunnable){
		String dimensionName = WorldUtils.getDimensionName(level);
		if(!dimensionTickQueueMap.containsKey(dimensionName)){
			RdiCoreServer.LOGGER.info("维度{}没有延迟tick队列，正在创建",dimensionName);
			dimensionTickQueueMap.put(dimensionName,EvictingQueue.create(queueSize));
		}
		dimensionTickQueueMap.get(dimensionName).add(tickableRunnable);

	}
	public static void removeDimension(String dimensionName){
		dimensionTickQueueMap.remove(dimensionName);
		RdiCoreServer.LOGGER.info("已删除维度{}的延迟tick队列",dimensionName);
	}
	public static int getQueueSize(String dimensionName){
		EvictingQueue<Runnable> queue = dimensionTickQueueMap.get(dimensionName);
		if(queue == null)
			return 0;
		else
			return queue.size();
	}
	public static void onServerTick(){
		if(ServerLaggingStatus.isServerLagging())
			return;
		dimensionTickQueueMap.forEach((dimensionName,queue)->{
			if(queue.peek() != null){
				ServerUtils.executeOnServerThread(()->{
					queue.poll().run();
				});
			}
		});

	}
}
