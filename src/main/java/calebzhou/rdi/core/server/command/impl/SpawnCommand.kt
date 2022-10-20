package calebzhou.rdi.core.server.command.impl

import calebzhou.rdi.core.server.command.RdiNormalCommand
import calebzhou.rdi.core.server.utils.PlayerUtils
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import net.minecraft.commands.CommandSourceStack

class SpawnCommand : RdiNormalCommand("spawn", "回到主城") {
    override val execution : LiteralArgumentBuilder<CommandSourceStack>
    get() = baseArgBuilder
            .executes { context: CommandContext<CommandSourceStack> ->
                val player = context.source.player!!
                PlayerUtils.teleportToSpawn(player)
                PlayerUtils.sendChatMessage(player, PlayerUtils.RESPONSE_SUCCESS, "成功回到了主城！")
                1
            }

}
