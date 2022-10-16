package calebzhou.rdi.core.server.command.impl

import calebzhou.rdi.core.server.RdiMemoryStorage
import calebzhou.rdi.core.server.command.RdiCommand
import calebzhou.rdi.core.server.constant.ColorConst
import calebzhou.rdi.core.server.utils.PlayerUtils
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import net.minecraft.commands.CommandSourceStack

class HelpCommand : RdiCommand("rdi-help") {
    override fun getExecution(): LiteralArgumentBuilder<CommandSourceStack> {
        return baseArgBuilder.executes { context: CommandContext<CommandSourceStack> ->
            val player = context.source.player!!
            PlayerUtils.sendChatMessage(player, ColorConst.BRIGHT_GREEN + "=====RDI帮助菜单=====")
            RdiMemoryStorage.commandSet.parallelStream().forEach { command: RdiCommand ->
                if(command.displayInHelp){
                    PlayerUtils.sendChatMessage(
                        player,
                        "/${command.commandName}：${command.description}"
                    )
                }

            }
            1
        }
    }
}
