package calebzhou.rdi.core.server.utils

import calebzhou.rdi.core.server.RdiCoreServer.Companion.server
import calebzhou.rdi.core.server.RdiMemoryStorage
import calebzhou.rdi.core.server.constant.ColorConst
import calebzhou.rdi.core.server.constant.NetworkPackets.DIALOG_INFO
import calebzhou.rdi.core.server.constant.NetworkPackets.POPUP
import calebzhou.rdi.core.server.constant.RdiSharedConstants
import calebzhou.rdi.core.server.constant.RdiSharedConstants.SPAWN_LOCATION
import calebzhou.rdi.core.server.misc.RdiPlayerProfileManager
import calebzhou.rdi.core.server.model.*
import calebzhou.rdi.core.server.utils.TimeUtils.timeChineseString
import calebzhou.rdi.core.server.utils.WorldUtils.getDimensionName
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import net.minecraft.ChatFormatting
import net.minecraft.commands.CommandSourceStack
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket.RelativeArgument
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.level.TicketType
import net.minecraft.util.Mth
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.Vec3
import org.quiltmc.qsl.networking.api.PacketByteBufs
import org.quiltmc.qsl.networking.api.ServerPlayNetworking
import java.util.*
import java.util.function.Consumer
import java.util.function.Predicate

/**
 * Created by calebzhou on 2022-10-16,21:37.
 */
object PlayerUtils {
    const val RESPONSE_SUCCESS = 2
    const val RESPONSE_WARNING = 1
    const val RESPONSE_INFO = 0
    const val RESPONSE_ERROR = -1
    val RESPONSE_ERROR_PREFIX = Component.literal("错误 >").withStyle(ChatFormatting.DARK_RED).withStyle(ChatFormatting.BOLD)
    val RESPONSE_SUCCESS_PREFIX = Component.literal("成功 >").withStyle(ChatFormatting.DARK_GREEN).withStyle(ChatFormatting.BOLD)
    val RESPONSE_INFO_PREFIX = Component.literal("提示 >").withStyle(ChatFormatting.AQUA).withStyle(ChatFormatting.BOLD)
    val RESPONSE_WARNING_PREFIX = Component.literal("警告 >").withStyle(ChatFormatting.GOLD).withStyle(ChatFormatting.BOLD)

    fun sendPacketToClient(player:Player, pack: ResourceLocation, content:Any) {
		val buf = PacketByteBufs.create();
		if(content is Int)
			buf.writeInt(content);
		else if(content is Double)
			buf.writeDouble(content);
		else if(content is Float )
			buf.writeFloat(content);
		else if(content is Long )
			buf.writeLong(content);
		else if(content is CompoundTag)
			buf.writeNbt(content);
		else
			buf.writeUtf(content as String);
		ServerPlayNetworking.send(player as ServerPlayer,pack,buf);
	}
    fun getPlayersInLevel(player: Player): List<ServerPlayer> {
        return getPlayersInLevel(player.level)
    }

    fun getPlayersInLevel(level: Level): List<ServerPlayer> {
        return getPlayersInLevel(level) { true }
    }

    fun getPlayersInLevel(level: Level, condition: Predicate<in ServerPlayer>): List<ServerPlayer> {
        return (level as ServerLevel).getPlayers(condition)
    }
    fun broadcastMessageToLevel(level: ServerLevel, component: Component, actionBar: Boolean) {
        getPlayersInLevel(level!!).forEach(Consumer { player: ServerPlayer ->
            sendChatMessage(
                player,
                component,
                actionBar
            )
        })
    }

    fun broadcastMessageToLevel(level: Level, component: Component, actionBar: Boolean) {
        broadcastMessageToLevel(level as ServerLevel, component, actionBar)
    }

    fun sendMessageToCommandSource(source: CommandSourceStack, content: String) {
        sendMessageToCommandSource(source, Component.literal(content))
    }

    fun sendMessageToCommandSource(source: CommandSourceStack, textComponent: Component) {
        source.sendSuccess(textComponent, false)
    }

    fun isStandOnBlock(player: Player, block: Block): Boolean {
        val onPos = player.onPos
        return block === player.getLevel().getBlockState(onPos).block
    }

    fun sendChatMessage(player: Player, textComponent: Component, isOnActionBar: Boolean) {
        player.displayClientMessage(textComponent, isOnActionBar)
    }

    fun sendServiceResponseData(player: Player, data: ResponseData<*>) {
        sendChatMessage(player, if (data.stat > 0) RESPONSE_SUCCESS else RESPONSE_ERROR, data.msg)
    }
    fun sendChatMessage(player: Player, textComponent: Component) {
        sendChatMessage(player!!, textComponent, false)
    }

    fun sendChatMessage(player: Player, content: String) {
        sendChatMessage(player, Component.literal(content))
    }

    fun sendChatMessage(player: Player, messageType: Int) {
        sendChatMessage(player, messageType, "")
    }

    fun sendChatMessage(player: Player, messageType: Int, content: String) {
        when (messageType) {
            RESPONSE_SUCCESS -> sendChatMessage(player, RESPONSE_SUCCESS_PREFIX .append( content))
            RESPONSE_WARNING -> sendChatMessage(player, RESPONSE_WARNING_PREFIX .append( content))
            RESPONSE_INFO -> sendChatMessage(player, RESPONSE_INFO_PREFIX .append(content))
            RESPONSE_ERROR -> sendChatMessage(player, RESPONSE_ERROR_PREFIX .append( content))
        }
    }

    fun sendChatMultilineMessage(player: Player, content: String) {
        val split = content.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (line in split) {
            sendChatMessage(player, line)
        }
    }

    //添加生物效果
    fun addEffect(player: ServerPlayer, effect: MobEffect, seconds: Int, amplifier: Int) {
        player.addEffect(MobEffectInstance(effect, seconds * 20, amplifier))
    }

    //是否是新手
    fun isFreshPlayer(player: Player): Boolean {
        return player.experienceLevel == 0
    }

    //是否在主城
    fun isInMainTown(player: Player): Boolean {
        val onPos = player.onPos
        return (onPos.x > -256 && onPos.x < 256 && onPos.z > -256 && onPos.z < 256
                && PlayerUtils.isInOverworld(player))
    }

    /**
     * 是否在主世界
     */
    fun isInOverworld(player: Player): Boolean {
        return player.getLevel() === server.overworld()
    }

    /**
     * 是否在末地
     */
    fun isInTheEnd(player: Player): Boolean {
        return player.getLevel() === server.getLevel(Level.END)
    }

    /**
     * 是否在地狱
     */
    fun isInNether(player: Player): Boolean {
        return player.getLevel() === server.getLevel(Level.NETHER)
    }

    /**
     * 是否在岛屿世界
     */
    fun isInIsland(player: Player): Boolean {
        return getDimensionName(player.getLevel())
            .startsWith(RdiSharedConstants.ISLAND_DIMENSION_FULL_PREFIX)
    }

    fun sendClientPopup(player: Player, type: String, title: String, msg: String) {
        PlayerUtils.sendPacketToClient(player, POPUP, String.format("%s|%s|%s", type, title, msg))
    }

    fun sendClientDialog(player: Player, type: String, title: String, msg: String) {
        PlayerUtils.sendPacketToClient(player, DIALOG_INFO, String.format("%s|%s|%s", type, title, msg))
    }

    fun teleportToSpawn(player: Player) {
        teleport(player, server.overworld(), RdiSharedConstants.SPAWN_LOCATION)
    }

    fun teleport(player: Player, lvl: ServerLevel, bpos: BlockPos) {
        teleport(player, lvl, bpos.x.toDouble(), bpos.y.toDouble(), bpos.z.toDouble(), 0.0, 0.0)
    }

    //传送1到2 玩家
    fun teleport(player1: Player, player2: Player) {
        teleport(
            player1, player2.getLevel() as ServerLevel, player2.x,
            player2.y, player2.z, player2.yRot.toDouble(), player2.xRot.toDouble()
        )
    }

    fun teleport(player: Player, playerLocation: RdiPlayerLocation) {
        teleport(
            player,
            playerLocation.level,
            playerLocation.x,
            playerLocation.y,
            playerLocation.z,
            playerLocation.w,
            playerLocation.p
        )
    }

    //传送到指定位置
    fun teleport(player: Player, world: ServerLevel, x: Double, y: Double, z: Double, yaw: Double, pitch: Double) {
        var world = world
        if (world == null) world = server.overworld()
        val blockPos = BlockPos(x, y, z)
        val warpYaw = Mth.wrapDegrees(yaw).toFloat()
        val warpPitch = Mth.wrapDegrees(pitch).toFloat()
        val chunkPos = ChunkPos(blockPos)
        world!!.chunkSource.addRegionTicket(TicketType.POST_TELEPORT, chunkPos, 1, player.id)
        player.stopRiding()
        if (player.isSleeping) {
            player.stopSleepInBed(true, true)
        }
        if (world === player.level) {
            (player as ServerPlayer).connection.teleport(
                x, y, z, warpPitch, warpYaw, EnumSet.noneOf(
                    RelativeArgument::class.java
                )
            )
        } else {
            (player as ServerPlayer).teleportTo(world, x, y, z, warpPitch, warpYaw)
        }
        player.setYHeadRot(warpYaw)
    }

    fun getPlayerLookingBlockPosition(player: Player, isFluid: Boolean): BlockPos {
        val rays = player.pick(64.0, 0.0f, isFluid) as BlockHitResult
        return rays.blockPos
    }

    fun getPlayerByName(name: String): ServerPlayer? {
        return server.playerList.getPlayerByName(name)
    }

    fun getPlayerByUuid(uuid: String): ServerPlayer? {
        return getPlayerByUuid(UUID.fromString(uuid))
    }

    fun getPlayerByUuid(uuid: UUID): ServerPlayer? {
        return server.playerList.getPlayer(uuid)
    }

    fun setSpawnPoint(player: ServerPlayer, levelResourceKey: ResourceKey<Level>, blockPos: BlockPos) {
        player.setRespawnPosition(levelResourceKey, blockPos, 0f, true, true)
    }

    fun satisfyMainTownBuildCondition(player: Player): Boolean {
        val rdiPlayerProfile = RdiPlayerProfileManager.pidProfileMap[player.stringUUID]?:return false
        return rdiPlayerProfile.isGenuine || player.experienceLevel >= 50
    }

    fun resetProfile(player: ServerPlayer) {
        player.experienceLevel = 0
        player.inventory.clearContent()
        player.kill()
        teleport(player, server.overworld(), SPAWN_LOCATION)
        setSpawnPoint(player, Level.OVERWORLD, SPAWN_LOCATION)
    }

    fun teleport(player: ServerPlayer, level: ServerLevel, vec3: Vec3) {
        teleport(player, level, vec3.x, vec3.y, vec3.z, 0.0, 0.0)
    }

    fun getPasswordStorageFile(player: Player): String {
        return ".minecraft/mods/rdi/users/" + player.stringUUID + "_password.txt"
    }

    fun sayHello(player: ServerPlayer) {
        sendChatMessage(
            player,
            timeChineseString + "好," + player.displayName.string + ColorConst.AQUA + "。输入/rdi-help打开帮助菜单"
        )
    }



    fun getAllPlayers(): List<ServerPlayer> {
        return server.playerList.players
    }

    val provinceCodeMap = Object2ObjectOpenHashMap<String, Int>()

}
