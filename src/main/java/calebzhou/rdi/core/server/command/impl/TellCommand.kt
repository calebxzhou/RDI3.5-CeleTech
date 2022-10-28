package calebzhou.rdi.core.server.command.impl

import calebzhou.rdi.core.server.command.RdiNormalCommand
import calebzhou.rdi.core.server.utils.PlayerUtils
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.commands.arguments.MessageArgument
import net.minecraft.network.chat.Component

/**
 * Created by calebzhou on 2022-09-26,20:52.
 */
class TellCommand : RdiNormalCommand("msg", "私聊",true) {
    override val execution : LiteralArgumentBuilder<CommandSourceStack>
    get() = baseArgBuilder.then(
            Commands.argument("player", EntityArgument.player())
                .then(
                    Commands.argument("msg", MessageArgument.message())
                        .executes { context: CommandContext<CommandSourceStack> -> exec(context) })
        ).executes { context: CommandContext<CommandSourceStack> ->
            PlayerUtils.sendMessageToCommandSource(context.source, "请")
            1
        }
    private fun exec(context: CommandContext<CommandSourceStack>): Int {
        val fromPlayer = context.source.player!!
        val toPlayer = EntityArgument.getPlayer(context, "player")
        val msg = MessageArgument.getMessage(context, "msg")
        PlayerUtils.sendChatMessage(
            toPlayer,
            Component.literal("${fromPlayer.scoreboardName}私聊您：").append(msg)
        )
        return 1
    }
}
