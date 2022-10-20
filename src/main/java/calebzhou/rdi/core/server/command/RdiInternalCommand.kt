package calebzhou.rdi.core.server.command

import calebzhou.rdi.core.server.constant.RdiSharedConstants
import calebzhou.rdi.core.server.constant.ResponseCode
import calebzhou.rdi.core.server.utils.PlayerUtils
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.arguments.MessageArgument

/**
 * Created by calebzhou on 2022-10-07,7:50.
 */
abstract class RdiInternalCommand(final override val commandName: String):RdiCommand{
    override val execution: LiteralArgumentBuilder<CommandSourceStack>
        get() =  Commands.literal(RdiSharedConstants.InternalCommandPrefix+commandName)
            .then(Commands.argument("arg",MessageArgument.message()).executes(::executeInternal))

    private fun executeInternal(context: CommandContext<CommandSourceStack>): Int {
        if (!context.source.hasPermission(4)){
            PlayerUtils.sendChatMessage(context.source.player!!,"您没权利用内部指令！")
            return 1
        }
        val arg = MessageArgument.getMessage(context,"arg")
        //参数用|分割
        if(execute(context,arg.string.split("|")))
            feedback(context.source,ResponseCode.Success)
        return 1
    }
    //返回指令执行结果标号给微服务
    protected fun feedback(src:CommandSourceStack, responseCode: ResponseCode){
        PlayerUtils.sendMessageToCommandSource(src,responseCode.code.toString())
    }
    abstract fun execute(context: CommandContext<CommandSourceStack>, arg: List<String>):Boolean

}
