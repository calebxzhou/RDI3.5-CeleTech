package calebzhou.rdi.core.server.command.impl

import calebzhou.rdi.core.server.RdiMemoryStorage
import calebzhou.rdi.core.server.command.RdiCommand
import calebzhou.rdi.core.server.constant.ColorConst
import calebzhou.rdi.core.server.utils.PlayerUtils
import calebzhou.rdi.core.server.utils.ThreadPool
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.server.level.ServerPlayer

class TpaCommand : RdiCommand("tpa", "传送到一个玩家身边") {
    override fun getExecution(): LiteralArgumentBuilder<CommandSourceStack> {
        return baseArgBuilder.then(
            Commands.argument("玩家", EntityArgument.player())
                .executes { context: CommandContext<CommandSourceStack> ->
                    exec(
                        context.source.player,
                        EntityArgument.getPlayer(context, "玩家")
                    )
                })
    }

    private fun exec(fromPlayer: ServerPlayer?, toPlayer: ServerPlayer?): Int {
        val fromPlayerId = fromPlayer!!.stringUUID
        if (toPlayer == null) {
            PlayerUtils.sendChatMessage(fromPlayer, PlayerUtils.RESPONSE_ERROR, "对方不在线！")
            return 1
        }
        val toPlayerId = toPlayer.stringUUID
        /*if(fromPlayerId.equals(toPlayerId)){
            sendChatMessage(fromPlayer, RESPONSE_ERROR,"禁止原地TP");
            return 1;
        }*/if (fromPlayer.experienceLevel < 3) {
            PlayerUtils.sendChatMessage(fromPlayer, PlayerUtils.RESPONSE_ERROR, "经验不足,您需要3级经验.")
            return 1
        }
        if (RdiMemoryStorage.tpaMap.containsKey(fromPlayer.stringUUID)) {
            PlayerUtils.sendChatMessage(fromPlayer, PlayerUtils.RESPONSE_ERROR, "您已经发送过传送请求了")
            return 1
        }
        RdiMemoryStorage.tpaMap[toPlayerId] = fromPlayerId
        PlayerUtils.sendChatMessage(
            fromPlayer,
            PlayerUtils.RESPONSE_SUCCESS,
            "已经向%s发送了传送请求，15秒后传送请求将失效。".formatted(toPlayer.scoreboardName)
        )
        fromPlayer.experienceLevel -= 3
        PlayerUtils.sendChatMessage(toPlayer, ColorConst.ORANGE + fromPlayer.scoreboardName + "想要传送到你的身边。")
        PlayerUtils.sendChatMessage(toPlayer, ColorConst.ORANGE + fromPlayer.scoreboardName + "要接受，输入/tpyes")
        ThreadPool.doAfter(15) { RdiMemoryStorage.tpaMap.remove(toPlayerId) }
        return 1
    }
}
