package calebzhou.rdi.core.server.command.impl

import calebzhou.rdi.core.server.RdiMemoryStorage
import calebzhou.rdi.core.server.command.RdiCommand
import calebzhou.rdi.core.server.utils.PlayerUtils
import calebzhou.rdi.core.server.utils.ThreadPool
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet
import net.minecraft.commands.CommandSourceStack
import net.minecraft.server.level.ServerPlayer
import java.util.*
import java.util.function.Consumer

/**
 * Created by calebzhou on 2022-10-01,8:55.
 */
class SpeakScopeCommand : RdiCommand("speak-scope", "设定聊天范围，是岛内可见还是全服可见") {
    override fun getExecution(): LiteralArgumentBuilder<CommandSourceStack> {
        return baseArgBuilder.executes { context: CommandContext<CommandSourceStack> -> exec(context) }
    }

    private fun exec(context: CommandContext<CommandSourceStack>): Int {
        ThreadPool.newThread {
            val player = context.source.player!!
            val pid = player!!.stringUUID
            if (RdiMemoryStorage.pidToSpeakPlayersMap.containsKey(pid) && RdiMemoryStorage.pidToSpeakPlayersMap[pid]!!.size > 0) {
                if (RdiMemoryStorage.pidToSpeakPlayersMap.remove(pid) != null) {
                    PlayerUtils.sendChatMessage(player, PlayerUtils.RESPONSE_SUCCESS, "您的聊天范围已经更改为：全服务器")
                } else {
                    PlayerUtils.sendChatMessage(player, PlayerUtils.RESPONSE_INFO, "聊天范围已经更改为：全服务器（不知道成没成功，你先试试）")
                }
                return@newThread
            }
            if (!PlayerUtils.isInIsland(player)) {
                PlayerUtils.sendChatMessage(player, PlayerUtils.RESPONSE_ERROR, "必须在自己的岛屿上才能使用此功能！")
                return@newThread
            }
            val playersInLevel = PlayerUtils.getPlayersInLevel(player)
            if (playersInLevel.size == 0) {
                PlayerUtils.sendChatMessage(player, PlayerUtils.RESPONSE_ERROR, "您的岛屿上目前没有成员，需要等待成员上线以后才能使用此功能！")
                return@newThread
            }
            val playerSet = ObjectOpenHashSet<String>()
            playerSet.addAll(playersInLevel.stream().map { obj: ServerPlayer -> obj.stringUUID }.toList())
            RdiMemoryStorage.pidToSpeakPlayersMap[pid] = playerSet
            PlayerUtils.sendChatMessage(
                player,
                PlayerUtils.RESPONSE_SUCCESS,
                "成功将聊天范围设定为：岛屿内可见。您的聊天消息只有以下玩家可见：" + Arrays.toString(
                    playerSet.stream().map { uuid: String -> PlayerUtils.getPlayerByUuid(uuid) }
                        .toArray()))
            playersInLevel.forEach(Consumer { playerInLevel: ServerPlayer ->
                PlayerUtils.sendChatMessage(
                    playerInLevel,
                    PlayerUtils.RESPONSE_INFO,
                    "岛屿成员%s将聊天范围设定为了 仅岛屿内可见 ，您可以通过/speak-scope指令来设置。".formatted(
                        player.scoreboardName
                    )
                )
            })
        }
        return 1
    }
}
