package calebzhou.rdi.core.server.ticking

import calebzhou.rdi.core.server.utils.ServerUtils.broadcastChatMessage
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.minecraft.SharedConstants
import net.minecraft.gametest.framework.GameTestTicker
import net.minecraft.network.protocol.game.ClientboundSetTimePacket
import net.minecraft.server.MinecraftServer
import net.minecraft.world.level.GameRules
import java.util.function.BooleanSupplier

/**
 * Created  on 2022-10-27,9:06.
 */
object WorldTicking {
    @JvmStatic
    fun onTick(
        hasTimeLeft: BooleanSupplier,
        server: MinecraftServer,
        tickables: MutableList<Runnable>
    ){
        server.profiler.push("commandFunctions")
        server.functions.tick()
        server.profiler.popPush("levels")

        runBlocking {
            for (serverLevel in server.allLevels) {
                launch {
                    server.profiler.push { "$serverLevel ${serverLevel.dimension().location()} " }
                    if (server.tickCount % 20 == 0) {
                        server.profiler.push("timeSync")
                        server.playerList.broadcastAll(
                            ClientboundSetTimePacket(serverLevel.gameTime, serverLevel.dayTime, serverLevel.gameRules.getBoolean(GameRules.RULE_DAYLIGHT)),
                            serverLevel.dimension())
                        server.profiler.pop()
                    }
                    server.profiler.push("tick")
                    try {
                        serverLevel.tick(hasTimeLeft)
                    } catch (e: Throwable) {
                        e.printStackTrace()
                        broadcastChatMessage("tick world错误" + e.message + e.cause)
                    }
                    server.profiler.pop()
                    server.profiler.pop()
                }
            }
        }


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
