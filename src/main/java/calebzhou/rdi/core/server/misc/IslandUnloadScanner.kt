package calebzhou.rdi.core.server.misc

import calebzhou.rdi.core.server.RdiCoreServer
import calebzhou.rdi.core.server.logger
import calebzhou.rdi.core.server.ticking.TickTaskManager
import calebzhou.rdi.core.server.utils.IslandUtils
import calebzhou.rdi.core.server.utils.ServerUtils
import calebzhou.rdi.core.server.utils.WorldUtils
import it.unimi.dsi.fastutil.objects.Object2ObjectMap
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import net.minecraft.server.level.ServerLevel
import xyz.nucleoid.fantasy.Fantasy
import xyz.nucleoid.fantasy.mixin.MinecraftServerAccess
import java.util.*

/**
 * Created by calebzhou on 2022-09-29,21:05.
 */
object IslandUnloadScanner : TimerTask() {

    init {
        //每1分钟检查一次岛屿有没有人 没人就卸载了
        Timer().schedule(this, 0L, 1*60 * 1000L)
    }

    override fun run() {
        RdiCoreServer.server.allLevels.forEach {level->
            if(WorldUtils.isNoPlayersInLevel(level)&& WorldUtils.isInIsland2(level)){
                logger.info("岛屿${WorldUtils.getDimensionName(level)}没有玩家了，即将卸载")
                ServerUtils.executeOnServerThread {
                    Fantasy.get(RdiCoreServer.server).unloadWorld(level)
                }
            }
        }
    }

       /* @JvmStatic
		fun addIslandToUnloadQueue(level: ServerLevel) {
            val dimensionName = WorldUtils.getDimensionName(level)
            if (!WorldUtils.isInIsland2(level)) {
                logger.info("{} 不是岛屿维度 不会加入卸载队列", dimensionName)
                return
            }
            if (isIslandInQueue(level)) {
                logger.info("卸载队列里面已经有{}了 不会加入卸载队列", dimensionName)
                return
            }
            dimNameLevelMapToUnload[dimensionName] = level
            logger.info("{} 已经添加到维度卸载队列", dimensionName)
        }

        @JvmStatic
		fun isIslandInQueue(level: ServerLevel): Boolean {
            return dimNameLevelMapToUnload.containsKey(WorldUtils.getDimensionName(level)) || dimNameLevelMapToUnload.containsValue(
                level
            )
        }

        fun removeIslandFromQueue(level: ServerLevel) {
            removeIslandFromQueue(WorldUtils.getDimensionName(level))
        }
        fun clearQueue(){
            dimNameLevelMapToUnload.clear()
            logger.info("全部删除 维度卸载队列")
        }
        fun removeIslandFromQueue(dimensionName: String) {
            logger.info("{}即将移除岛屿卸载队列", dimensionName)
            dimNameLevelMapToUnload.remove(dimensionName)
        }

        private fun processUnloadQueue() {
            if (dimNameLevelMapToUnload.size == 0) return
            dimNameLevelMapToUnload.object2ObjectEntrySet().parallelStream()
                .forEach { (dimensionName, levelToUnload): Object2ObjectMap.Entry<String, ServerLevel> ->
                    if (!WorldUtils.isInIsland2(levelToUnload)) {
                        logger.info("{}不是岛屿维度 不卸载", dimensionName)
                        removeIslandFromQueue(dimensionName)
                        return@forEach
                    }
                    if (!WorldUtils.isNoPlayersInLevel(levelToUnload)) {
                        logger.info("岛屿{}还有玩家 不卸载", dimensionName)
                        removeIslandFromQueue(dimensionName)
                        return@forEach
                    }
                    logger.info("岛屿" + dimensionName + "没有玩家了，即将卸载")
                    TickTaskManager.removeDimension(dimensionName)
                    ServerUtils.executeOnServerThread {
                        Fantasy.get(RdiCoreServer.server).unloadWorld(levelToUnload)
                    }
                    dimNameLevelMapToUnload.remove(dimensionName)
                }
            //ServerLevel levelToUnload = levelQueue.poll();
        }*/
}
