package calebzhou.rdi.core.server.command.impl

import calebzhou.rdi.core.server.command.RdiCommand
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import net.minecraft.commands.CommandSourceStack

/**
 * Created by calebzhou on 2022-09-29,19:59.
 */
class InspectCommand : RdiCommand("inspect") {
    override fun getExecution(): LiteralArgumentBuilder<CommandSourceStack> {
        return baseArgBuilder.executes { context: CommandContext<CommandSourceStack> ->
            val player = context.source.player
            1
        }
    }
}
