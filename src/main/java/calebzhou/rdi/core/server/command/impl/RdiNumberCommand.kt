package calebzhou.rdi.core.server.command.impl

import calebzhou.rdi.core.server.command.RdiNormalCommand
import calebzhou.rdi.core.server.utils.PlayerUtils
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import net.minecraft.ChatFormatting
import net.minecraft.commands.CommandSourceStack
import net.minecraft.network.chat.ClickEvent
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.HoverEvent
import net.minecraft.network.chat.Style

/**
 * Created by calebzhou on 2022-09-28,12:42.
 */
class RdiNumberCommand : RdiNormalCommand("rdi-number", "查询自己的RDI编号",true) {
    override val execution : LiteralArgumentBuilder<CommandSourceStack>
    get() = baseArgBuilder.executes { context -> exec(context).toInt() }

    private fun exec(context: CommandContext<CommandSourceStack>): Short {
        val player = context.source.player!!
        val pid = player.stringUUID
        PlayerUtils.sendChatMessage(player, PlayerUtils.RESPONSE_INFO, "您的编号是：")
        PlayerUtils.sendChatMessage(player, Component.literal(pid).withStyle { style: Style ->
            style.withColor(ChatFormatting.GREEN)
                .withClickEvent(ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, pid))
                .withHoverEvent(HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("chat.copy.click")))
        })
        PlayerUtils.sendChatMessage(player, PlayerUtils.RESPONSE_INFO, "点击编号可以复制")
        return 1
    }
}
