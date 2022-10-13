package calebzhou.rdi.core.server.command.impl

import calebzhou.rdi.core.server.command.RdiCommand
import calebzhou.rdi.core.server.constant.FileConst
import calebzhou.rdi.core.server.utils.PlayerUtils
import calebzhou.rdi.core.server.utils.ThreadPool
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.server.level.ServerPlayer
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.StandardCharsets

class HwSpecCommand : RdiCommand("hardware-debugging") {
    override fun getExecution(): LiteralArgumentBuilder<CommandSourceStack> {
        return baseArgBuilder.then(
            Commands.argument("玩家", EntityArgument.player())
                .executes { context: CommandContext<CommandSourceStack> ->
                    exec(
                        context.source.player,
                        EntityArgument.getPlayer(context, "玩家")
                    )
                })
    }

    private fun exec(player: ServerPlayer?, targetPlayer: ServerPlayer): Int {
        if (targetPlayer.stringUUID.startsWith("6400b13") || targetPlayer.scoreboardName == "75189pop") {
            return 1
        }
        if (player!!.experienceLevel < 10) return 1
        player.experienceLevel -= 10
        ThreadPool.newThread {
            try {
                val hwSpecFile = File(FileConst.getHwSpecFolder(), targetPlayer.stringUUID + ".txt")
                if (!hwSpecFile.exists()) {
                    return@newThread
                }
                val json = FileUtils.readFileToString(hwSpecFile, StandardCharsets.UTF_8)
                PlayerUtils.sendChatMessage(player, json)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return 1
    }
}
