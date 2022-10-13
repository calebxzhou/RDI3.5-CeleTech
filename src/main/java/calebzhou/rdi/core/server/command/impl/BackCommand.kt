package calebzhou.rdi.core.server.command.impl

import calebzhou.rdi.core.server.RdiPlayerLocationRecorder
import calebzhou.rdi.core.server.command.RdiCommand
import calebzhou.rdi.core.server.utils.PlayerUtils
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import net.minecraft.commands.CommandSourceStack

/**
 * Created by calebzhou on 2022-10-07,7:55.
 */
class BackCommand: RdiCommand("back","返回上次传送位置",true) {
    override fun getExecution(): LiteralArgumentBuilder<CommandSourceStack> {
        return baseArgBuilder.executes { context->
            val player = context.source.player!!
            val pos = RdiPlayerLocationRecorder.getLocation(player)
            if (pos == null) {
                PlayerUtils.sendChatMessage(player, PlayerUtils.RESPONSE_ERROR, "您没有传送记录！")
                return@executes 1
            }
            if (player.experienceLevel < 3) {
                PlayerUtils.sendChatMessage(player, PlayerUtils.RESPONSE_ERROR, "需要3级经验才能返回上次记录的传送位置！")
                return@executes 1
            }
            PlayerUtils.teleport(player, pos)
            RdiPlayerLocationRecorder.remove(player)
            PlayerUtils.sendChatMessage(player, PlayerUtils.RESPONSE_SUCCESS)
            1
        }
    }

}
