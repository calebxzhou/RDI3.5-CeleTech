package calebzhou.rdi.core.server.command.impl

import calebzhou.rdi.core.server.misc.CommandConfirmer
import calebzhou.rdi.core.server.RdiCoreServer
import calebzhou.rdi.core.server.command.RdiCommand
import calebzhou.rdi.core.server.misc.IslandUnloadManager
import calebzhou.rdi.core.server.utils.*
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import it.unimi.dsi.fastutil.Pair
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.SharedSuggestionProvider
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.phys.Vec3
import xyz.nucleoid.fantasy.Fantasy
import java.util.concurrent.CompletableFuture

class IslandCommand : RdiCommand("is", "岛屿菜单。", true) {
    override fun getExecution(): LiteralArgumentBuilder<CommandSourceStack> {
        return baseArgBuilder.then(
                Commands.argument("指令参数", StringArgumentType.string())
                    .suggests { context: CommandContext<CommandSourceStack>, builder: SuggestionsBuilder ->
                        getSuggestion(
                            context,
                            builder
                        )
                    }
                    .executes { context: CommandContext<CommandSourceStack> ->
                        handleSubCommand(
                            context.source.player!!,
                            StringArgumentType.getString(context, "指令参数")
                        )
                    }
                    .then(
                        Commands.argument("玩家名", EntityArgument.player())
                            .executes { context: CommandContext<CommandSourceStack> ->
                                handleSubCommandWithPlayerNameParam(
                                    StringArgumentType.getString(context, "指令参数"),
                                    context.source.player!!,
                                    EntityArgument.getPlayer(context, "玩家名")
                                )
                            }
                    )
            )
    }

    protected fun getSuggestion(
        context: CommandContext<CommandSourceStack>,
        builder: SuggestionsBuilder
    ): CompletableFuture<Suggestions> {
        return SharedSuggestionProvider.suggest(
            arrayOf(
                "create", "reset", "kick", "invite", "loca", "transfer", "quit"
            ), builder
        )
    }

    private fun handleSubCommandWithPlayerNameParam(
        param: String,
        fromPlayer: ServerPlayer,
        toPlayer: ServerPlayer
    ): Int {
        when (param) {
            "invite" -> invitePlayer(fromPlayer, toPlayer)
            "kick" -> kickPlayer(fromPlayer, toPlayer)
            "transfer" -> transferIsland(fromPlayer, toPlayer)
            else -> 1
        }
        return 1
    }

    private fun handleSubCommand(player: ServerPlayer, param: String): Int {
        when (param) {
            "create" -> createIsland(player)
            "reset" -> resetIsland(player)
            "loca" -> locateIsland(player)
            "quit" -> quitIsland(player)
            else -> 1
        }
        return 1
    }


    private fun resetIsland(player: ServerPlayer) {
        PlayerUtils.sendChatMessage(player, PlayerUtils.RESPONSE_WARNING, "真的，要重置这个岛屿吗？所有的数据将会被删除！")
        CommandConfirmer.addConfirm(player) { player: ServerPlayer -> confirmedResetIsland(player) }
    }

    private fun confirmedResetIsland(player: ServerPlayer) {
        ThreadPool.newThread {
            val ResponseData = RdiHttpClient.sendRequest(Int::class, "delete", "/v37/mcs_game/island2/" + player.stringUUID)
            if (ResponseData.isSuccess) {
                val dim = IslandUtils.getIslandDimensionLoca(ResponseData.data!!)
                IslandUnloadManager.removeIslandFromQueue(dim.toString())
                ServerUtils.executeOnServerThread {
                    val worldHandle = Fantasy.get(RdiCoreServer.server)
                        .getOrOpenPersistentWorld(dim, IslandUtils.getIslandWorldConfig(0))
                    PlayerUtils.resetProfile(player)
                    worldHandle.delete()
                    PlayerUtils.sendChatMessage(player, PlayerUtils.RESPONSE_SUCCESS)
                }
            }
        }
    }

    private fun quitIsland(player: ServerPlayer) {
        PlayerUtils.sendChatMessage(player, PlayerUtils.RESPONSE_WARNING, "真的，要退出这个岛屿吗？您的个人全部数据，将会被删除！")
        CommandConfirmer.addConfirm(player) { player: ServerPlayer -> confirmedQuitIsland(player) }
    }

    private fun confirmedQuitIsland(player: ServerPlayer) {
        ThreadPool.newThread {
            val ResponseData = RdiHttpClient.sendRequest("delete", "/v37/mcs_game/island2/crew/" + player.stringUUID)
            if (ResponseData.isSuccess) {
                ServerUtils.executeOnServerThread {
                    PlayerUtils.resetProfile(player)
                    PlayerUtils.sendChatMessage(player, PlayerUtils.RESPONSE_SUCCESS)
                }
            } else PlayerUtils.sendServiceResponseData(player, ResponseData)
        }
    }

    private fun transferIsland(fromPlayer: ServerPlayer, toPlayer: ServerPlayer) {
        if (fromPlayer === toPlayer) {
            PlayerUtils.sendChatMessage(fromPlayer, PlayerUtils.RESPONSE_ERROR, "目标玩家不能是自己！")
            return
        }
        PlayerUtils.sendChatMessage(fromPlayer, PlayerUtils.RESPONSE_WARNING, "真的，要转让这个岛屿吗？您的个人全部数据，将会被删除！")
        CommandConfirmer.addConfirm(
            fromPlayer
        ) { fromPlayerAfterConfirm: ServerPlayer -> confirmedTransferToPlayer(fromPlayerAfterConfirm, toPlayer) }
    }

    private fun confirmedTransferToPlayer(fromPlayer: ServerPlayer, toPlayer: ServerPlayer) {
        ThreadPool.newThread {
            val ResponseData = RdiHttpClient.sendRequest(
                "put",
                "/v37/mcs_game/island2/transfer/" + fromPlayer.stringUUID + "/" + toPlayer.stringUUID
            )
            if (ResponseData.isSuccess) {
                PlayerUtils.sendChatMessage(fromPlayer, PlayerUtils.RESPONSE_SUCCESS)
                PlayerUtils.sendChatMessage(
                    toPlayer,
                    PlayerUtils.RESPONSE_SUCCESS,
                    fromPlayer.scoreboardName + "把岛屿转让给了你！"
                )
            } else PlayerUtils.sendServiceResponseData(fromPlayer, ResponseData)
        }
    }

    private fun createIsland(player: ServerPlayer) {
        PlayerUtils.sendChatMessage(player, PlayerUtils.RESPONSE_INFO, "准备创建岛屿，不要触碰鼠标或者键盘！")
        ThreadPool.newThread {
            val ResponseData = RdiHttpClient.sendRequest(Int::class, "post", "/v37/mcs_game/island2/" + player!!.stringUUID)
            if (!ResponseData.isSuccess) {
                PlayerUtils.sendServiceResponseData(player, ResponseData)
                return@newThread
            }
            PlayerUtils.sendChatMessage(player, PlayerUtils.RESPONSE_INFO, "开始创建岛屿，不要触碰鼠标或者键盘！")
            val iid = ResponseData.data!!
            PlayerUtils.sendChatMessage(player, PlayerUtils.RESPONSE_INFO, "您的岛屿ID：$iid")
            val server = RdiCoreServer.server
            ServerUtils.executeOnServerThread {
                val fantasy = Fantasy.get(server)
                PlayerUtils.sendChatMessage(player, PlayerUtils.RESPONSE_INFO, "正在创建存档。。不要触碰鼠标或者键盘！")
                val islandDimension = IslandUtils.getIslandDimensionLoca(iid)
                val worldHandle = fantasy.getOrOpenPersistentWorld(islandDimension, IslandUtils.getIslandWorldConfig(0))
                val level = worldHandle.asWorld()
                IslandUtils.placeInitialBlocks(level)
                PlayerUtils.addEffect(player, MobEffects.SLOW_FALLING, 10, 2)
                PlayerUtils.setSpawnPoint(player, level.dimension(), WorldUtils.INIT_POS.above(2))
                PlayerUtils.sendChatMessage(player, PlayerUtils.RESPONSE_INFO, "准备传送。。。")
                PlayerUtils.teleport(player, level, Vec3.atCenterOf(WorldUtils.INIT_POS).add(0.0, 7.0, 0.0))
                PlayerUtils.sendChatMessage(player, PlayerUtils.RESPONSE_SUCCESS, "成功！以后用H键就可以回到您的岛屿了！")
            }
        }
    }

    private fun locateIsland(player: ServerPlayer) {
        if (!PlayerUtils.isInIsland(player)) {
            PlayerUtils.sendChatMessage(player, PlayerUtils.RESPONSE_ERROR, "必须在您自己的岛屿上，才能设定传送点！")
            return
        }
        if (!PlayerUtils.isStandOnBlock(player, Blocks.OBSIDIAN)) {
            PlayerUtils.sendChatMessage(player, PlayerUtils.RESPONSE_ERROR, "必须在一块黑曜石上，才能设定传送点！")
            return
        }
        PlayerUtils.sendChatMessage(
            player, PlayerUtils.RESPONSE_WARNING, "真的要，将这个岛屿的传送点，更改为您目前所在的位置，（%s）吗？"
                .formatted(player!!.onPos.toShortString())
        )
        CommandConfirmer.addConfirm(player) { player: ServerPlayer -> confirmLocateIsland(player) }
    }

    private fun confirmLocateIsland(player: ServerPlayer) {
        val x = player.x
        val y = player.y
        val z = player.z
        val w = player.xRot.toDouble()
        val p = player.yRot.toDouble()
        ThreadPool.newThread {
            val ResponseData = RdiHttpClient.sendRequest(
                "put", "/v37/mcs_game/island2/loca/" + player.stringUUID,
                Pair.of("x", x),
                Pair.of("y", y),
                Pair.of("z", z),
                Pair.of("w", w),
                Pair.of("p", p)
            )
            if (ResponseData.isSuccess) {
                PlayerUtils.sendChatMessage(player, PlayerUtils.RESPONSE_SUCCESS, "将这个岛屿的传送点，更改为您目前所在的位置了！")
            } else PlayerUtils.sendServiceResponseData(player, ResponseData)
        }
    }

    private fun kickPlayer(fromPlayer: ServerPlayer, kickPlayer: ServerPlayer) {
        if (fromPlayer === kickPlayer) {
            PlayerUtils.sendChatMessage(fromPlayer, PlayerUtils.RESPONSE_ERROR, "您不可以踢出自己!!")
            return
        }
        PlayerUtils.sendChatMessage(
            fromPlayer,
            PlayerUtils.RESPONSE_WARNING,
            "真的，要踢出玩家" + kickPlayer.scoreboardName + "吗？他的全部数据将会被删除！"
        )
        CommandConfirmer.addConfirm(
            fromPlayer
        ) { fromPlayerAfterConfirm: ServerPlayer -> confirmKickPlayer(fromPlayerAfterConfirm, kickPlayer) }
    }

    private fun confirmKickPlayer(fromPlayer: ServerPlayer, kickPlayer: ServerPlayer) {
        ThreadPool.newThread {
            val ResponseData = RdiHttpClient.sendRequest("delete", "/v37/mcs_game/island2/crew/" + kickPlayer.stringUUID)
            if (ResponseData.isSuccess) {
                PlayerUtils.sendChatMessage(
                    kickPlayer,
                    PlayerUtils.RESPONSE_WARNING,
                    fromPlayer.scoreboardName + "删除了他的岛屿!"
                )
                PlayerUtils.resetProfile(kickPlayer)
                PlayerUtils.sendChatMessage(fromPlayer, PlayerUtils.RESPONSE_SUCCESS)
            } else PlayerUtils.sendServiceResponseData(fromPlayer, ResponseData)
        }
    }

    private fun invitePlayer(player: ServerPlayer, invitedPlayer: ServerPlayer) {
        if (player === invitedPlayer) {
            PlayerUtils.sendChatMessage(player, PlayerUtils.RESPONSE_ERROR, "目标玩家不能是自己！")
            return
        }
        ThreadPool.newThread {
            val ResponseData = RdiHttpClient.sendRequest(
                "post",
                "/v37/mcs_game/island2/crew/" + player!!.stringUUID + "/" + invitedPlayer.stringUUID
            )
            if (ResponseData.isSuccess) {
                PlayerUtils.sendChatMessage(player, "您邀请了$invitedPlayer")
                PlayerUtils.sendChatMessage(
                    invitedPlayer,
                    PlayerUtils.RESPONSE_INFO,
                    player.scoreboardName + "邀请您加入了他的岛屿"
                )
                PlayerUtils.sendChatMessage(invitedPlayer, "按下[H键]可以前往他的岛屿")
                PlayerUtils.sendChatMessage(player, PlayerUtils.RESPONSE_SUCCESS, "1")
            } else PlayerUtils.sendServiceResponseData(player, ResponseData)
        }
    }

}
