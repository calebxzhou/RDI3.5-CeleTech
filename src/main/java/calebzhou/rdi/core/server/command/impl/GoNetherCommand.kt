package calebzhou.rdi.core.server.command.impl

import calebzhou.rdi.core.server.command.RdiNormalCommand
import calebzhou.rdi.core.server.utils.PlayerUtils
import calebzhou.rdi.core.server.utils.WorldUtils
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.Blocks

class GoNetherCommand : RdiNormalCommand("go-nether", "下地狱", true) {
    override val execution: LiteralArgumentBuilder<CommandSourceStack>
        get() = baseArgBuilder.executes(::exec)


    private fun exec(context: CommandContext<CommandSourceStack>): Int {
        val player = context.source.player!!
        if (!PlayerUtils.isInIsland(player)) {
            PlayerUtils.sendChatMessage(player, PlayerUtils.RESPONSE_ERROR, "只有在您的岛屿才能通过此功能前往地狱！")
            return 1
        }
        if (player.experienceLevel < 3) {
            PlayerUtils.sendChatMessage(player, PlayerUtils.RESPONSE_ERROR, "必须有3级经验才能前往地狱！")
            return 1
        }
        val targetPos = WorldUtils.getIsland2ToNetherPos(WorldUtils.getIsland2IdInt(player.getLevel()))
        WorldUtils.placeBlock(WorldUtils.nether, BlockPos(targetPos), Blocks.OBSIDIAN.defaultBlockState())
        PlayerUtils.teleport(
            player,
            WorldUtils.nether,
            targetPos.x.toDouble(),
            (targetPos.y + 3).toDouble(),
            targetPos.z.toDouble(),
            0.0,
            0.0
        )
        PlayerUtils.sendChatMessage(player, PlayerUtils.RESPONSE_SUCCESS, "1")
        player.experienceLevel -= 3
        return 1
    }
}


