package calebzhou.rdi.core.server.command

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.arguments.MessageArgument

/**
 * Created  on 2022-10-18,8:55.
 */
//玩家远程提交给微服务执行的指令
abstract class RdiPlayerRemoteCommand(commandName: String,
                                      description: String
): RdiNormalCommand(commandName, description, true) {
    override val execution : LiteralArgumentBuilder<CommandSourceStack>
        get() = baseArgBuilder.then(Commands.argument("arg",MessageArgument.message()).executes(::executeInternal))

    private fun executeInternal(context: CommandContext<CommandSourceStack>): Int {
        val player = context.source.player!!

        val pid = player.stringUUID
        val arg = MessageArgument.getMessage(context,"arg").string

        return 1
    }

}
