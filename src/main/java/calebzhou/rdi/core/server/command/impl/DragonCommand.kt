package calebzhou.rdi.core.server.command.impl

import calebzhou.rdi.core.server.command.RdiNormalCommand
import calebzhou.rdi.core.server.utils.PlayerUtils
import calebzhou.rdi.core.server.utils.ServerUtils
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.world.entity.EntityType

class DragonCommand : RdiNormalCommand("dragon", "召唤一只末影龙", true) {
    private val expLvlNeed = 100
    override val execution : LiteralArgumentBuilder<CommandSourceStack>
    get() = baseArgBuilder.executes { context: CommandContext<CommandSourceStack> ->
            val player = context.source.player!!
            if (!PlayerUtils.isInTheEnd(player)) {
                PlayerUtils.sendChatMessage(player, PlayerUtils.RESPONSE_ERROR, "必须在末地执行本指令")
                return@executes 1
            }
            if (player.experienceLevel < expLvlNeed) {
                PlayerUtils.sendChatMessage(player, PlayerUtils.RESPONSE_ERROR, "您需要有" + expLvlNeed + "级经验才能召唤神龙！")
                return@executes 1
            }
            PlayerUtils.sendChatMessage(player, PlayerUtils.RESPONSE_SUCCESS, "成功召唤神龙！")
            ServerUtils.spawnEntity(EntityType.ENDER_DRAGON, player.getLevel(), player.onPos.above(50))
            player.experienceLevel -= expLvlNeed
            1
        }

}
