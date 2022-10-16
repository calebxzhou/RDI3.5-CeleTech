package calebzhou.rdi.core.server.command.impl

import calebzhou.rdi.core.server.RdiCoreServer
import calebzhou.rdi.core.server.command.RdiCommand
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.network.chat.Component

class SaveCommand : RdiCommand("SAVE", "存档。") {
    override fun getExecution(): LiteralArgumentBuilder<CommandSourceStack> {
        return baseArgBuilder.executes { c: CommandContext<CommandSourceStack> ->
            RdiCoreServer.server.saveEverything(true, true, true)
            c.source.sendSuccess(Component.literal("成功"), true)
            1
        }
    }
}
