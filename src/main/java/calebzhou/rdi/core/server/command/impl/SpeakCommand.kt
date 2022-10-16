package calebzhou.rdi.core.server.command.impl

import calebzhou.rdi.core.server.RdiCoreServer
import calebzhou.rdi.core.server.RdiMemoryStorage
import calebzhou.rdi.core.server.command.RdiCommand
import calebzhou.rdi.core.server.utils.EncodingUtils
import calebzhou.rdi.core.server.utils.PlayerUtils
import calebzhou.rdi.core.server.utils.RdiHttpClient
import calebzhou.rdi.core.server.utils.ServerUtils
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import it.unimi.dsi.fastutil.Pair
import net.minecraft.ChatFormatting
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.arguments.MessageArgument
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.PlayerChatMessage
import java.util.function.Consumer

/**
 * Created by calebzhou on 2022-09-19,11:58.
 */
class SpeakCommand : RdiCommand("speak", "说话") {
    override fun getExecution(): LiteralArgumentBuilder<CommandSourceStack> {
        return baseArgBuilder.then(
            Commands.argument("msg", MessageArgument.message())
                .executes { context: CommandContext<CommandSourceStack> ->
                    val player = context.source.player!!
                    val pid = player!!.stringUUID
                    val chatMessage = MessageArgument.getChatMessage(context, "msg")
                    chatMessage.resolve(context.source) { playerChatMessage: PlayerChatMessage ->
                        val txt = playerChatMessage.signedContent().plain()
                        if (RdiMemoryStorage.pidToSpeakPlayersMap.containsKey(pid)) {
                            RdiMemoryStorage.pidToSpeakPlayersMap[pid]!!.forEach(Consumer { pidToReceiveMsg: String ->
                                val receiver = PlayerUtils.getPlayerByUuid(pidToReceiveMsg)
                                if (receiver != null) {
                                    PlayerUtils.sendChatMessage(
                                        receiver, Component.literal(player.scoreboardName + "(岛内)：")
                                            .append(txt).withStyle(ChatFormatting.GOLD)
                                    )
                                    RdiCoreServer.LOGGER.info("{}岛内说：{}", player.scoreboardName, txt)
                                }
                            })
                        } else {
                            ServerUtils.broadcastChatMessage(Component.literal(player.scoreboardName + "：").append(txt))
                            RdiCoreServer.LOGGER.info("{}说：{}", player.scoreboardName, txt)
                        }
                        RdiHttpClient.sendRequestAsyncResponseless(
                            "post",
                            "/mcs/record/chat",
                            Pair.of("pid", pid),
                            Pair.of("cont", EncodingUtils.getUTF8StringFromGBKString(txt))
                        )
                    }
                    1
                })
    }
}
