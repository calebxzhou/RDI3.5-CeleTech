package calebzhou.rdi.core.server.command.impl

import calebzhou.rdi.core.server.command.RdiCommand
import calebzhou.rdi.core.server.utils.PlayerUtils
import calebzhou.rdi.core.server.utils.RdiHttpClient
import calebzhou.rdi.core.server.utils.ServerUtils
import calebzhou.rdi.core.server.utils.ThreadPool
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects

class Home1Command : RdiCommand("home1", "回到一岛（仅限老玩家）", false) {
    override fun getExecution(): LiteralArgumentBuilder<CommandSourceStack> {
        return baseArgBuilder.executes { context: CommandContext<CommandSourceStack> ->
            val player = context.source.player
            ThreadPool.newThread {
                val resultData =
                    RdiHttpClient.sendRequest(String::class.java, "get", "/v37/island/" + player!!.stringUUID)
                if (resultData.status < 0) {
                    PlayerUtils.sendServiceResultData(player, resultData)
                    return@newThread
                }
                val split = resultData.data.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val x = split[0].toInt()
                val y = split[1].toInt()
                val z = split[2].toInt()
                ServerUtils.executeOnServerThread {
                    player.addEffect(MobEffectInstance(MobEffects.SLOW_FALLING, 20 * 30, 1))
                    PlayerUtils.teleport(
                        player,
                        player.getServer()!!.overworld(),
                        x + 0.5,
                        (y + 2).toDouble(),
                        z + 0.5,
                        0.0,
                        0.0
                    )
                }
                PlayerUtils.sendChatMessage(player, PlayerUtils.RESPONSE_SUCCESS, "1")
            }
            1
        }
    }
}
