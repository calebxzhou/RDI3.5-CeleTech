package calebzhou.rdi.core.server.command.impl

import calebzhou.rdi.core.server.RdiCoreServer
import calebzhou.rdi.core.server.command.RdiNormalCommand
import calebzhou.rdi.core.server.constant.ColorConst
import calebzhou.rdi.core.server.misc.ServerLaggingStatus
import calebzhou.rdi.core.server.ticking.TickTaskManager
import calebzhou.rdi.core.server.utils.PlayerUtils
import calebzhou.rdi.core.server.utils.ThreadPool
import calebzhou.rdi.core.server.utils.WorldUtils
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import net.minecraft.commands.CommandSourceStack
import net.minecraft.network.chat.Component
import java.util.*
import kotlin.math.roundToInt

class TpsCommand : RdiNormalCommand("tps", "查询服务器的流畅程度",true) {
    override val execution : LiteralArgumentBuilder<CommandSourceStack>
    get() = baseArgBuilder.executes { context: CommandContext<CommandSourceStack> -> exec(context.source) }


    val squarePattern1 = ">"

    //100%负载tick时间
    val stdTickTime = 70.0
    val displayMaxMemory = 1023
    private fun exec(sourceStack: CommandSourceStack): Int {
        ThreadPool.newThread {

            //平均tick时间
            val meanTickTime = Arrays.stream(RdiCoreServer.server.tickTimes).average().asDouble * 1.0E-6
            //平均tps
            val meanTPS = (1000.0 / meanTickTime).coerceAtMost(20.0)
            //平均tick时间 比 100%负载tick时间
            val ratio = meanTickTime / stdTickTime
            //字符数
            val squares = (25 * ratio).roundToInt()
            val squaresToSend = StringBuilder(ColorConst.BRIGHT_GREEN)
            for (i in 0..squares) {
                squaresToSend.append(squarePattern1)
                if (i == 15) squaresToSend.append(ColorConst.GOLD)
                if (i == 22) squaresToSend.append(ColorConst.RED)
            }
            PlayerUtils.sendMessageToCommandSource(
                sourceStack,
                "%d%%/%.2fTPS/%.2fms%s".format((ratio * 100).roundToInt(), meanTPS, meanTickTime, squaresToSend)
            )
            val dimensionName = WorldUtils.getDimensionName(sourceStack.level)
            val queueSize = TickTaskManager.getQueueSize(dimensionName)
            PlayerUtils.sendMessageToCommandSource(
                sourceStack,
                "延迟?%s %sms 任务数%s "
                    .format(
                        if (ServerLaggingStatus.isServerLagging) "是" else "否",
                        ServerLaggingStatus.msBehind,
                        queueSize
                    )
            )
            val totalMemory = Runtime.getRuntime().totalMemory()
            val memoryUsed = totalMemory - Runtime.getRuntime().freeMemory()
            val memoryUsage = memoryUsed.toFloat() / totalMemory.toFloat()
            PlayerUtils.sendMessageToCommandSource(
                sourceStack,
                "ram=%.1fMB/%dMB(%.1f%%)".format(displayMaxMemory * memoryUsage, displayMaxMemory, memoryUsage * 100)
            )
            if (queueSize > 0) sourceStack.sendSuccess(delayTickStatus[dimensionName], false)
        }
        return 1
    }

    companion object {
        @JvmField
        val delayTickStatus = Object2ObjectOpenHashMap<String, Component>()
    }
}
