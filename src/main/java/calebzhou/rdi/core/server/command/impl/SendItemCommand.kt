package calebzhou.rdi.core.server.command.impl

import calebzhou.rdi.core.server.command.RdiCommand
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.arguments.EntityArgument

/**
 * Created by calebzhou on 2022-10-02,16:51.
 */
class SendItemCommand:RdiCommand("send-item","向玩家远程发送手上的物品") {
    override fun getExecution(): LiteralArgumentBuilder<CommandSourceStack> {

        return baseArgBuilder.then(
            Commands.argument("玩家",EntityArgument.player())
            .executes(this::exec)
        )
    }


    private fun exec(context: CommandContext<CommandSourceStack>): Int {
        val player = context.source.player

        return 1;
    }
}
