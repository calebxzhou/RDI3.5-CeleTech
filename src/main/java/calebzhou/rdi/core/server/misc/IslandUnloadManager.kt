package calebzhou.rdi.core.server.misc

import calebzhou.rdi.core.server.RdiCoreServer
import calebzhou.rdi.core.server.utils.ServerUtils
import calebzhou.rdi.core.server.utils.WorldUtils
import it.unimi.dsi.fastutil.objects.Object2ObjectMap
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import net.minecraft.server.level.ServerLevel
import xyz.nucleoid.fantasy.Fantasy
import java.util.*

/**
 * Created by calebzhou on 2022-09-29,21:05.
 */
class IslandUnloadManager : TimerTask() {
    override fun run() {
        processUnloadQueue()
    }

    companion object {
        private val INSTANCE = IslandUnloadManager()

        init {
            //每分钟检查一次维度卸载队列
            Timer().schedule(INSTANCE, 0L, 60 * 1000L)
        }

        private val dimNameLevelMap = Object2ObjectOpenHashMap<String, ServerLevel>()
        @JvmStatic
		fun addIslandToUnloadQueue(level: ServerLevel) {
            val dimensionName = WorldUtils.getDimensionName(level)
            if (!WorldUtils.isInIsland2(level)) {
                RdiCoreServer.LOGGER.info("{} 不是岛屿维度 不会加入卸载队列", dimensionName)
                return
            }
            if (isIslandInQueue(level)) {
                RdiCoreServer.LOGGER.info("卸载队列里面已经有{}了 不会加入卸载队列", dimensionName)
                return
            }
            dimNameLevelMap[dimensionName] = level
            RdiCoreServer.LOGGER.info("{} 已经添加到维度卸载队列", dimensionName)
        }

        @JvmStatic
		fun isIslandInQueue(level: ServerLevel): Boolean {
            return dimNameLevelMap.containsKey(WorldUtils.getDimensionName(level)) || dimNameLevelMap.containsValue(
                level
            )
        }

        fun removeIslandFromQueue(level: ServerLevel?) {
            removeIslandFromQueue(WorldUtils.getDimensionName(level))
        }

        fun removeIslandFromQueue(dimensionName: String) {
            RdiCoreServer.LOGGER.info("{}即将移除岛屿卸载队列", dimensionName)
            dimNameLevelMap.remove(dimensionName)
        }

        private fun processUnloadQueue() {
            if (dimNameLevelMap.size == 0) return
            dimNameLevelMap.object2ObjectEntrySet().parallelStream()
                .forEach { (dimensionName, levelToUnload): Object2ObjectMap.Entry<String, ServerLevel> ->
                    if (!WorldUtils.isInIsland2(levelToUnload)) {
                        RdiCoreServer.LOGGER.info("{}不是岛屿维度 不卸载", dimensionName)
                        removeIslandFromQueue(dimensionName)
                        return@forEach
                    }
                    if (!WorldUtils.isNoPlayersInLevel(levelToUnload)) {
                        RdiCoreServer.LOGGER.info("岛屿{}还有玩家 不卸载", dimensionName)
                        removeIslandFromQueue(dimensionName)
                        return@forEach
                    }
                    RdiCoreServer.LOGGER.info("岛屿" + dimensionName + "没有玩家了，即将卸载")
                    TickTaskManager.removeDimension(dimensionName)
                    ServerUtils.executeOnServerThread {
                        levelToUnload.save(null, true, false)
                        Fantasy.get(RdiCoreServer.getServer()).unloadWorld(levelToUnload)
                    }
                    dimNameLevelMap.remove(dimensionName)
                }
            //ServerLevel levelToUnload = levelQueue.poll();
        }
    }
}
