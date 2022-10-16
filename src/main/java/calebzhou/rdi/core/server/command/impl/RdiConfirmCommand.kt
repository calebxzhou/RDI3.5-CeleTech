package calebzhou.rdi.core.server.command.impl

import calebzhou.rdi.core.server.misc.CommandConfirmer
import calebzhou.rdi.core.server.command.RdiCommand
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands

/**
 * Created by calebzhou on 2022-09-18,9:27.
 */
class RdiConfirmCommand : RdiCommand("rdi-confirm", "确认执行指令。") {
    override fun getExecution(): LiteralArgumentBuilder<CommandSourceStack> {
        return baseArgBuilder.then(
            Commands.argument("confirm-id", StringArgumentType.string())
                .executes { context: CommandContext<CommandSourceStack> ->
                    CommandConfirmer.onCommandExecute(context)
                })
    }
}
