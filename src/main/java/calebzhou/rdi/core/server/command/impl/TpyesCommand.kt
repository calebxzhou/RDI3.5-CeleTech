package calebzhou.rdi.core.server.command.impl

import calebzhou.rdi.core.server.RdiMemoryStorage
import calebzhou.rdi.core.server.RdiPlayerLocationRecorder
import calebzhou.rdi.core.server.command.RdiCommand
import calebzhou.rdi.core.server.utils.PlayerUtils
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import net.minecraft.commands.CommandSourceStack
import org.apache.commons.lang3.StringUtils

class TpyesCommand : RdiCommand("tpyes", "接受传送请求") {
    override fun getExecution(): LiteralArgumentBuilder<CommandSourceStack> {
        return baseArgBuilder.executes { context: CommandContext<CommandSourceStack> ->
            val toPlayer = context.source.player
            if (toPlayer!!.experienceLevel < 3) {
                PlayerUtils.sendChatMessage(toPlayer, PlayerUtils.RESPONSE_ERROR, "经验不足,您需要3级经验.")
                return@executes 1
            }
            val fromPlayerId = RdiMemoryStorage.tpaMap[toPlayer.stringUUID]
            if (StringUtils.isEmpty(fromPlayerId)) {
                PlayerUtils.sendChatMessage(toPlayer, PlayerUtils.RESPONSE_ERROR, "找不到传送请求....")
                return@executes 1
            }
            val fromPlayer = PlayerUtils.getPlayerByUuid(fromPlayerId)
            RdiPlayerLocationRecorder.record(fromPlayer)
            if (fromPlayer == null) {
                PlayerUtils.sendChatMessage(toPlayer, PlayerUtils.RESPONSE_ERROR, "玩家不在线！")
                return@executes 1
            }
            PlayerUtils.sendChatMessage(toPlayer, "正在传送..")
            PlayerUtils.sendChatMessage(fromPlayer, "正在传送..")
            PlayerUtils.teleport(fromPlayer, toPlayer)
            toPlayer.experienceLevel -= 3
            RdiMemoryStorage.tpaMap.remove(toPlayer.stringUUID)
            1
        }
    }
}
