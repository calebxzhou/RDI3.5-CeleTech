package calebzhou.rdi.core.server.command.impl

import calebzhou.rdi.core.server.command.RdiCommand
import calebzhou.rdi.core.server.utils.PlayerUtils
import calebzhou.rdi.core.server.utils.WorldUtils
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.core.Vec3i
import net.minecraft.world.level.block.Blocks

/**
 * Created by calebzhou on 2022-09-18,16:11.
 */
class MeltObsidianCommand : RdiCommand("melt-obsidian", "熔化黑曜石") {
    override fun getExecution(): LiteralArgumentBuilder<CommandSourceStack> {
        return baseArgBuilder.executes { context: CommandContext<CommandSourceStack> ->
            val player = context.source.player
            val posLook = PlayerUtils.getPlayerLookingBlockPosition(player, false)
            if (posLook == Vec3i(0, 127, 0)) {
                PlayerUtils.sendChatMessage(player, PlayerUtils.RESPONSE_ERROR, "不能熔化原点方块！")
                return@executes 1
            }
            if (Blocks.OBSIDIAN === player!!.getLevel().getBlockState(posLook).block) {
                WorldUtils.placeBlock(player!!.getLevel(), posLook, Blocks.LAVA)
                PlayerUtils.sendChatMessage(player, PlayerUtils.RESPONSE_SUCCESS, "成功融化黑曜石")
            } else {
                PlayerUtils.sendChatMessage(player, PlayerUtils.RESPONSE_ERROR, "准星必须对准黑曜石")
            }
            1
        }
    }
}
