package calebzhou.rdi.core.server.misc

import calebzhou.rdi.core.server.RdiCoreServer
import calebzhou.rdi.core.server.ServerLaggingStatus
import calebzhou.rdi.core.server.utils.WorldUtils
import com.google.common.collect.EvictingQueue
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import net.minecraft.world.level.Level

/**
 * Created by calebzhou on 2022-09-26,8:21.
 */
object TickTaskManager {
    private const val queueSize = 3 * 1024 * 1024

    //维度名vsTick队列
    private val dimensionTickQueueMap = Object2ObjectOpenHashMap<String, EvictingQueue<Runnable>>()
	fun addDelayTickTask(level: Level, tickableRunnable: Runnable) {
        val dimensionName = WorldUtils.getDimensionName(level)
        if (!dimensionTickQueueMap.containsKey(dimensionName)) {
            RdiCoreServer.LOGGER.info("维度{}没有延迟tick队列，正在创建", dimensionName)
            dimensionTickQueueMap[dimensionName] =
                EvictingQueue.create(queueSize)
        }
        dimensionTickQueueMap[dimensionName]!!.add(tickableRunnable)
    }
    fun removeDimension(dimensionName: String) {
        dimensionTickQueueMap.remove(dimensionName)
        RdiCoreServer.LOGGER.info("已删除维度{}的延迟tick队列", dimensionName)
    }

    fun getQueueSize(dimensionName: String): Int {
        val queue = dimensionTickQueueMap[dimensionName]
        return queue?.size ?: 0
    }
	fun onServerTick() {
        if (ServerLaggingStatus.isServerLagging()) return
        dimensionTickQueueMap.forEach { (dimensionName: String, queue: EvictingQueue<Runnable>) ->
            if (queue.peek() != null) {
                queue.poll()!!.run()
            }
        }
    }
}
