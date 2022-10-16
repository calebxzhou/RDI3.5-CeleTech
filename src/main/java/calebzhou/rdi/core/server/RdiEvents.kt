package calebzhou.rdi.core.server

import calebzhou.rdi.core.server.RdiCoreServer.Companion.server
import calebzhou.rdi.core.server.command.RdiCommand
import calebzhou.rdi.core.server.command.impl.*
import calebzhou.rdi.core.server.constant.NetworkPackets
import calebzhou.rdi.core.server.misc.IslandUnloadManager
import calebzhou.rdi.core.server.misc.PlayerLocationRecorder.record
import calebzhou.rdi.core.server.misc.ServerLaggingStatus.isServerLagging
import calebzhou.rdi.core.server.misc.TickTaskManager.onServerTick
import calebzhou.rdi.core.server.model.RdiGeoLocation
import calebzhou.rdi.core.server.model.RdiPlayerProfile
import calebzhou.rdi.core.server.model.RdiWeather
import calebzhou.rdi.core.server.model.ResponseData
import calebzhou.rdi.core.server.module.DeathRandomDrop
import calebzhou.rdi.core.server.utils.*
import calebzhou.rdi.core.server.utils.PlayerUtils.getAllPlayers
import calebzhou.rdi.core.server.utils.PlayerUtils.getPasswordStorageFile
import calebzhou.rdi.core.server.utils.PlayerUtils.isInIsland
import calebzhou.rdi.core.server.utils.PlayerUtils.sayHello
import calebzhou.rdi.core.server.utils.PlayerUtils.teleportToSpawn
import calebzhou.rdi.core.server.utils.RdiHttpClient.sendRequest
import calebzhou.rdi.core.server.utils.RdiHttpClient.sendRequestAsyncResponseless
import calebzhou.rdi.core.server.utils.WorldUtils.getDimensionName
import calebzhou.rdi.core.server.utils.WorldUtils.isInIsland2
import calebzhou.rdi.core.server.utils.WorldUtils.isNoPlayersInLevel
import com.mojang.brigadier.CommandDispatcher
import it.unimi.dsi.fastutil.Pair
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents.AfterPlayerChange
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents.AllowDeath
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents
import net.minecraft.commands.CommandBuildContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands.CommandSelection
import net.minecraft.core.BlockPos
import net.minecraft.core.Registry
import net.minecraft.network.chat.Component
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.network.ServerGamePacketListenerImpl
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import org.quiltmc.qsl.command.api.CommandRegistrationCallback
import org.quiltmc.qsl.lifecycle.api.event.ServerTickEvents
import org.quiltmc.qsl.networking.api.PacketSender
import org.quiltmc.qsl.networking.api.ServerPlayConnectionEvents
import java.util.function.Consumer

class RdiEvents private constructor() {
    fun register() {
        ServerTickEvents.END.register(ServerTickEvents.End { server: MinecraftServer -> onServerEndTick(server) })
        CommandRegistrationCallback.EVENT.register(CommandRegistrationCallback { dispatcher: CommandDispatcher<CommandSourceStack>, context: CommandBuildContext, selection: CommandSelection ->
            registerCommands(
                dispatcher,
                context,
                selection
            )
        })
        ServerPlayConnectionEvents.JOIN.register(ServerPlayConnectionEvents.Join { listener: ServerGamePacketListenerImpl, sender: PacketSender, server: MinecraftServer ->
            onPlayerJoin(
                listener,
                sender,
                server
            )
        })
        ServerPlayConnectionEvents.DISCONNECT.register(ServerPlayConnectionEvents.Disconnect { listener: ServerGamePacketListenerImpl, server: MinecraftServer ->
            onPlayerDisconnect(
                listener,
                server
            )
        })
        PlayerBlockBreakEvents.BEFORE.register(PlayerBlockBreakEvents.Before { level: Level, player: Player, pos: BlockPos, blockState: BlockState, blockEntity: BlockEntity ->
            beforeBreakBlock(
                level,
                player,
                pos,
                blockState,
                blockEntity
            )
        })
        PlayerBlockBreakEvents.AFTER.register(PlayerBlockBreakEvents.After { level: Level, player: Player, blockPos: BlockPos, state: BlockState, block: BlockEntity ->
            onAfterBreakBlock(
                level,
                player,
                blockPos,
                state,
                block
            )
        })
        //UseBlockCallback.EVENT.register(this::onUseBlock);
        ServerPlayerEvents.ALLOW_DEATH.register(AllowDeath { player: ServerPlayer, source: DamageSource, damage: Float ->
            onPlayerDeath(
                player,
                source,
                damage
            )
        })
        ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register(AfterPlayerChange { player: ServerPlayer, fromLevel: ServerLevel, toLevel: ServerLevel ->
            onPlayerChangeWorld(
                player,
                fromLevel,
                toLevel
            )
        })
    }

    private fun onServerEndTick(server: MinecraftServer) {
        for (i in 0..999) {
            if (!isServerLagging) onServerTick() else break
        }
    }

    /*private InteractionResult onUseBlock(Player player, Level level, InteractionHand hand, BlockHitResult result) {
		if(isInMainTown(player)  && satisfyMainTownBuildCondition(player) ){
			sendChatMessage(player, RESPONSE_ERROR,"要建造主城，您需要有50级经验或者使用微软账号登录。");
			return InteractionResult.FAIL;
		}

		return InteractionResult.PASS;

	}*/
    //改变世界之后
    private fun onPlayerChangeWorld(player: ServerPlayer, fromLevel: ServerLevel, toLevel: ServerLevel) {
        RdiCoreServer.LOGGER.info(
            "{}离开{}去{}",
            player.scoreboardName,
            getDimensionName(fromLevel),
            getDimensionName(toLevel)
        )
        //要去的维度在岛屿卸载队列里面
        if (IslandUnloadManager.isIslandInQueue(toLevel)) {
            IslandUnloadManager.removeIslandFromQueue(toLevel)
        } else  //离开的维度是岛屿维度 并且里面没人
            if (isInIsland2(fromLevel) && isNoPlayersInLevel(fromLevel)) {
                IslandUnloadManager.addIslandToUnloadQueue(fromLevel)
            }
    }

    //破坏方块之前
    private fun beforeBreakBlock(
        level: Level,
        player: Player,
        pos: BlockPos,
        blockState: BlockState,
        blockEntity: BlockEntity
    ): Boolean {
        if (isInMainTown(player) && !satisfyMainTownBuildCondition(player)) {
            sendChatMessage(player, RESPONSE_ERROR, "要破坏主城方块，您需要有50级经验或者使用微软账号登录。")
            return false
        }
        return true
    }

    //玩家死亡
    private fun onPlayerDeath(player: ServerPlayer, source: DamageSource, damage: Float): Boolean {
        val pid = player.stringUUID
        val src = source.getMsgId()
        //记录死亡
        sendRequestAsyncResponseless("post", "/v37/mcs_game/record/death", Pair.of("pid", pid), Pair.of("src", src))
        //随机掉落物品
        DeathRandomDrop.handleDeath(player)
        //记录地点
        record(player)
        return true
    }

    //玩家断开服务器
    private fun onPlayerDisconnect(listener: ServerGamePacketListenerImpl, server: MinecraftServer) {
        val player = listener.getPlayer()
        //移除挂机信息和传送信息
        RdiMemoryStorage.afkMap.removeInt(player.scoreboardName)
        val pid = player.stringUUID
        RdiMemoryStorage.tpaMap.remove(pid)
        RdiMemoryStorage.pidGeoMap.remove(pid)
        RdiMemoryStorage.pidWeatherMap.remove(pid)
        RdiMemoryStorage.pidUserMap.remove(pid)
        RdiMemoryStorage.pidBeingGoSpawn.remove(pid)
        RdiMemoryStorage.pidToSpeakPlayersMap.remove(pid)
        //记录登出信息
        sendRequestAsyncResponseless("post", "/v37/mcs_game/record/logout", Pair.of("pid", pid))
        RdiIslandUnloadManager.addIslandToUnloadQueue(player.getLevel())
    }

    //成功破坏方块
    private fun onAfterBreakBlock(
        level: Level,
        player: Player,
        blockPos: BlockPos,
        state: BlockState,
        block: BlockEntity
    ) {
        //发送破坏数据
        recordBlock(
            player.stringUUID,
            Registry.BLOCK.getKey(state.block).toString(),
            1,
            level,
            blockPos.x,
            blockPos.y,
            blockPos.z
        )
    }

    /*public static void handlePlayerGrowTree(Player player, SaplingBlock saplingBlock, Level level, BlockPos blockPos){
		if(player.getHealth()<1f)
			return;
		if(!player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty())
			return;
		saplingBlock.performBonemeal((ServerLevel) level,player.getRandom(),blockPos,level.getBlockState(blockPos));
		player.hurt(DamageSource.MAGIC,19.5f);
		sendChatMessage(player, RESPONSE_INFO,"您成功给小树输了营养，现在它已经长出来了捏~");
	}*/
    //玩家连接服务器 连接成功
    private fun onPlayerJoin(listener: ServerGamePacketListenerImpl, sender: PacketSender, server: MinecraftServer) {
        val player = listener.getPlayer()
        //准备传送到主城
        if (RdiMemoryStorage.pidBeingGoSpawn.contains(player.stringUUID)) {
            teleportToSpawn(player)
            RdiMemoryStorage.pidBeingGoSpawn.remove(player.stringUUID)
        }
        if (isInIsland(player)) RdiIslandUnloadManager.removeIslandFromQueue(player.getLevel())
        //提示账号是否有密码
        val pid = player.stringUUID
        ThreadPool.newThread {
            val ResponseData: ResponseData<Boolean?> =
                sendRequest(Boolean::class.java, "get", "/v37/account/isreg/$pid")
            val rdiPlayerProfile: RdiPlayerProfile = RdiMemoryStorage.pidUserMap.get(pid)
            if (rdiPlayerProfile == null) {
                player.connection.disconnect(Component.literal("用户对象不能为空"))
                return@newThread
            }
            if (ResponseData.data!!) {
                if (java.lang.Boolean.parseBoolean(ResponseData.data.toString())) {
                    val loginData = sendRequest("get", "/v37/account/login/$pid", Pair.of("pwd", rdiPlayerProfile.pwd))
                    if (!loginData.isSuccess) {
                        RdiCoreServer.LOGGER.info("此请求密码错误！")
                        player.connection.disconnect(
                            Component.literal(
                                """
    RDI密码错误，请尝试重启游戏
    或者检查文件${getPasswordStorageFile(player)}
    """.trimIndent()
                            )
                        )
                    }
                }
            } else {
                //只有盗版才提示
                if (rdiPlayerProfile.type == "legacy") {
                    sendClientPopup(player, "warning", "RDI账号安全", "建议使用/set-password指令设置密码")
                }
            }
        }
        ThreadPool.newThread {
            val request: ResponseData<RdiGeoLocation?> =
                sendRequest(RdiGeoLocation::class.java, "get", "/v37/public/ip2loca", Pair.of("ip", player.ipAddress))
            if (request.isSuccess) {
                val geoLocation = request.data
                val weatherResponseData: ResponseData<RdiWeather?> = sendRequest(
                    RdiWeather::class.java,
                    "get",
                    "/v37/public/weather",
                    Pair.of("longitude", geoLocation!!.location.longitude),
                    Pair.of("latitude", geoLocation.location.latitude)
                )
                if (weatherResponseData.isSuccess) {
                    val rdiWeather = weatherResponseData.data
                    PlayerUtils.saveGeoLocation(player, geoLocation)
                    PlayerUtils.saveWeather(player, rdiWeather)
                    NetworkUtils.sendPacketToClient(
                        player,
                        NetworkPackets.GEO_LOCATION,
                        RdiSerializer.GSON.toJson(geoLocation)
                    )
                    NetworkUtils.sendPacketToClient(
                        player,
                        NetworkPackets.WEATHER,
                        RdiSerializer.GSON.toJson(rdiWeather)
                    )
                    getAllPlayers().forEach(Consumer { pl: ServerPlayer ->
                        pl.connection.send(
                            ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.UPDATE_DISPLAY_NAME, player)
                        )
                    })
                    PlayerUtils.sendWeatherInfo(player, geoLocation, rdiWeather)
                    sayHello(player)
                    sendRequestAsyncResponseless(
                        "post", "/v37/mcs_game/record/login",
                        Pair.of("pid", pid),
                        Pair.of("ip", player.ipAddress),
                        Pair.of("geo", geoLocation.toString())
                    )
                    sendRequestAsyncResponseless(
                        "post", "/v37/mcs_game/record/idname",
                        Pair.of("pid", pid),
                        Pair.of("name", player.scoreboardName)
                    )
                }
            }
        }
    }

    //指令注册
    private fun registerCommands(
        dispatcher: CommandDispatcher<CommandSourceStack>,
        context: CommandBuildContext,
        selection: CommandSelection
    ) {
        commandSet.add(BackCommand())
        commandSet.add(TellCommand())
        commandSet.add(ChangeBiomeCommand())
        commandSet.add(DragonCommand())
        commandSet.add(GoNetherCommand())
        commandSet.add(HelpCommand())
        commandSet.add(Home1Command())
        commandSet.add(HomeCommand())
        commandSet.add(IslandCommand())
        commandSet.add(MeltObsidianCommand())
        commandSet.add(RdiConfirmCommand())
        commandSet.add(SaveCommand())
        commandSet.add(SetPasswordCommand())
        commandSet.add(SpawnCommand())
        commandSet.add(SpeakCommand())
        commandSet.add(TpaCommand())
        commandSet.add(TpsCommand())
        commandSet.add(TpyesCommand())
        commandSet.add(WeatherCommand())
        commandSet.add(RdiNumberCommand())
        commandSet.add(SpeakScopeCommand())
        commandSet.forEach(Consumer { cmd: RdiCommand ->
            if (cmd.getExecution() != null) {
                dispatcher.register(cmd.getExecution())
            }
        })
    }

    companion object {
        val INSTANCE = RdiEvents()

        //act 0放置1破坏
        @JvmStatic
        fun recordBlock(pid: String, bid: String, act: Int, world: Level, x: Int, y: Int, z: Int) {
            //只记录主世界主城以内
            if (world === server.overworld()) {
                val rangeInSpawn = x > -256 && x < 256 && z > -256 && z < 256
                if (rangeInSpawn) {
                    sendRequestAsyncResponseless(
                        "post", "/v37/mcs_game/record/block",
                        Pair.of("pid", pid),
                        Pair.of("bid", bid),
                        Pair.of("act", act),
                        Pair.of("world", world),
                        Pair.of("x", x),
                        Pair.of("y", y),
                        Pair.of("z", z)
                    )
                }
            }
        }
    }
}
