package calebzhou.rdi.core.server

import calebzhou.rdi.core.server.mixin.AccessServerLoginPacketListenerImpl
import calebzhou.rdi.core.server.model.RdiUser
import calebzhou.rdi.core.server.utils.RdiSerializer
import com.mojang.authlib.GameProfile
import net.minecraft.network.Connection
import net.minecraft.network.chat.Component
import net.minecraft.network.protocol.login.ServerboundHelloPacket
import net.minecraft.server.network.ServerLoginPacketListenerImpl
import java.util.*

/**
 * Created by calebzhou on 2022-09-18,21:08.
 */
object RdiLoginProtocol {
    @JvmStatic
    fun handleHello(
        connection: Connection,
        loginPacketListener: ServerLoginPacketListenerImpl,
        helloPacket: ServerboundHelloPacket
    ): Boolean {
        try {
            val json = helloPacket.name()
            RdiCoreServer.LOGGER.info("收到登录请求：{}", json)
            if (!json.contains("{")) {
                RdiCoreServer.LOGGER.info("此请求协议格式错误！")
                connection.disconnect(Component.literal("登录协议错误 格式错误1，请更新客户端！"))
                return false
            }
            val rdiUser = RdiSerializer.GSON.fromJson(json, RdiUser::class.java)
            (loginPacketListener as AccessServerLoginPacketListenerImpl).setGameProfile(
                GameProfile(
                    UUID.fromString(
                        rdiUser.uuid
                    ), rdiUser.name
                )
            )
            RdiMemoryStorage.pidUserMap[rdiUser.uuid] = rdiUser
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }
}
