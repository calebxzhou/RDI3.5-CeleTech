package calebzhou.rdi.core.server.command.impl

import calebzhou.rdi.core.server.RdiCoreServer
import calebzhou.rdi.core.server.command.RdiNormalCommand
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.network.chat.Component

class SaveCommand : RdiNormalCommand("SAVE", "存档。",true) {
    override val execution : LiteralArgumentBuilder<CommandSourceStack>
    get() = baseArgBuilder.executes { c: CommandContext<CommandSourceStack> ->
            RdiCoreServer.server.saveEverything(true, true, true)
            c.source.sendSuccess(Component.literal("成功"), true)
            1
        }

}
