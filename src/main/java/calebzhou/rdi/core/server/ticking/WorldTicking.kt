package calebzhou.rdi.core.server.ticking

import calebzhou.rdi.core.server.logger
import calebzhou.rdi.core.server.utils.ServerUtils.broadcastChatMessage
import calebzhou.rdi.core.server.utils.ThreadPool
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import net.minecraft.SharedConstants
import net.minecraft.gametest.framework.GameTestTicker
import net.minecraft.network.protocol.game.ClientboundSetTimePacket
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.GameRules
import java.util.concurrent.TimeUnit
import java.util.function.BooleanSupplier

/**
 * Created  on 2022-10-27,9:06.
 */
object WorldTicking {
    /*val tickThreads = Object2ObjectOpenHashMap<ServerLevel, TickThread>()

    class TickThread(dim: ServerLevel) : Thread() {
        val tickQueue
    }*/


    @JvmStatic
    fun onTick(
        hasTimeLeft: BooleanSupplier,
        server: MinecraftServer,
        tickables: MutableList<Runnable>
    ) {
        server.profiler.push("commandFunctions")
        server.functions.tick()
        server.profiler.popPush("levels")
        //val tickPool = ThreadPool.newPool("WorldTicking")
        for (serverLevel in server.allLevels) {
            //tickPool.execute {
            server.profiler.push { "$serverLevel ${serverLevel.dimension().location()} " }
            if (server.tickCount % 20 == 0) {
                server.profiler.push("timeSync")
                server.playerList.broadcastAll(
                    ClientboundSetTimePacket(
                        serverLevel.gameTime,
                        serverLevel.dayTime,
                        serverLevel.gameRules.getBoolean(GameRules.RULE_DAYLIGHT)
                    ),
                    serverLevel.dimension()
                )
                server.profiler.pop()
            }
            server.profiler.push("tick")
           // runBlocking{
           //     withTimeout(500){
                    try {
                        serverLevel.tick(hasTimeLeft)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        broadcastChatMessage("tick world错误" + e.message + e.cause)
                    }
              //  }
          //  }
            server.profiler.pop()
            server.profiler.pop()
            // }
            /*if(!tickThreads.contains(serverLevel)){
                logger.info("维度{}没有Tick线程 正在创建",serverLevel)
                val thread = TickThread(serverLevel)
                tickThreads[serverLevel]= thread
                thread.start()
            }
*/
        }
        //tickPool.shutdown()
        //等100毫秒
        //tickPool.awaitTermination(100,TimeUnit.MILLISECONDS)

        server.profiler.popPush("connection")
        server.connection?.tick()
        server.profiler.popPush("players")
        server.playerList.tick()
        if (SharedConstants.IS_RUNNING_IN_IDE) {
            GameTestTicker.SINGLETON.tick()
        }

        server.profiler.popPush("server gui refresh")

        for (i in tickables.indices) {
            tickables[i].run()
        }

        server.profiler.pop()
    }
}
