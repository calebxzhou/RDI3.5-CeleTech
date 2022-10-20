package calebzhou.rdi.core.server.command.impl

import calebzhou.rdi.core.server.RdiCoreServer
import calebzhou.rdi.core.server.command.RdiNormalCommand
import calebzhou.rdi.core.server.model.Island2
import calebzhou.rdi.core.server.model.Island2Vo
import calebzhou.rdi.core.server.utils.*
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import net.minecraft.commands.CommandSourceStack
import xyz.nucleoid.fantasy.Fantasy

class HomeCommand : RdiNormalCommand("home", "回到我的岛屿", true) {
    override val execution : LiteralArgumentBuilder<CommandSourceStack>
    get() = baseArgBuilder.executes { context: CommandContext<CommandSourceStack> ->
            val player = context.source.player!!
            PlayerUtils.sendChatMessage(player, PlayerUtils.RESPONSE_INFO, "开始返回您的岛屿，请稍等...")
            ThreadPool.newThread {
                val ResponseData = RdiHttpClient.sendRequest(Island2Vo::class,"get", "/v37/mcs_game/island2/"+player.stringUUID);
                if (!ResponseData.isSuccess) {
                    PlayerUtils.sendChatMessage(player, PlayerUtils.RESPONSE_ERROR, ResponseData.msg)
                    return@newThread
                }
                val island2 = ResponseData.data!!
                val dim = IslandUtils.getIslandDimensionLoca(island2.iid)
                ServerUtils.executeOnServerThread {
                    val gameTime = System.currentTimeMillis() - island2.createTime.time
                    val worldHandle = Fantasy.get(RdiCoreServer.server)
                        .getOrOpenPersistentWorld(dim, IslandUtils.getIslandWorldConfig(gameTime))
                    val world = worldHandle.asWorld()
                    PlayerUtils.teleport(
                        player,
                        world,
                        island2.loca.x,
                        island2.loca.y + 3,
                        island2.loca.z,
                        island2.loca.w,
                        island2.loca.p
                    )
                    PlayerUtils.sendChatMessage(player, PlayerUtils.RESPONSE_SUCCESS, "已经回到了您的岛屿！")
                }
            }
            1
        }

}
