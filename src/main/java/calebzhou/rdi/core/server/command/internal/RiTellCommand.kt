package calebzhou.rdi.core.server.command.internal

import calebzhou.rdi.core.server.command.RdiInternalCommand
import calebzhou.rdi.core.server.constant.ResponseCode
import calebzhou.rdi.core.server.utils.PlayerUtils
import com.mojang.brigadier.context.CommandContext
import com.mojang.datafixers.optics.profunctors.ReCartesian
import net.minecraft.commands.CommandSourceStack

/**
 * Created by calebzhou on 2022-10-17,8:56.
 */
class RiTellCommand:RdiInternalCommand("tell") {
    //uuid|内容
    override fun execute(context: CommandContext<CommandSourceStack>, arg: List<String>):Boolean {
        val pid= arg[0]
        val content = arg[1]
        val player = PlayerUtils.getPlayerByUuid(pid)?:let{
            feedback(context.source,ResponseCode.TargetOffline)
            return false
        }

        PlayerUtils.sendChatMessage(player,content)
        return true
    }

}
