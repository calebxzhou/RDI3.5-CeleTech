package calebzhou.rdi.core.server.command.impl

import calebzhou.rdi.core.server.command.RdiNormalCommand
import calebzhou.rdi.core.server.utils.PlayerUtils
import calebzhou.rdi.core.server.utils.RdiHttpClient
import calebzhou.rdi.core.server.utils.ServerUtils
import calebzhou.rdi.core.server.utils.ThreadPool
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects

class Home1Command : RdiNormalCommand("home1", "回到一岛（仅限老玩家）", false) {
    override val execution : LiteralArgumentBuilder<CommandSourceStack>
    get() = baseArgBuilder.executes { context: CommandContext<CommandSourceStack> ->
            val player = context.source.player!!
            ThreadPool.newThread {
                val data = RdiHttpClient.sendRequest(String::class, "get", "/v37/island/" + player.stringUUID)
                if (!data.isSuccess) {
                    PlayerUtils.sendServiceResponseData(player, data)
                    return@newThread
                }
                val split = data.data!!.split(",")
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
