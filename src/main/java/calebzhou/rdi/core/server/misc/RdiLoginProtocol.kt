package calebzhou.rdi.core.server.misc

import calebzhou.rdi.core.server.logger
import calebzhou.rdi.core.server.mixin.AccessServerLoginPacketListenerImpl
import calebzhou.rdi.core.server.model.RdiPlayerProfile
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
            logger.info("收到登录请求：{}", json)
            if (!json.contains("{")) {
                logger.info("此请求协议格式错误！")
                connection.disconnect(Component.literal("登录协议错误 格式错误1，请更新客户端！"))
                return false
            }
            val rdiPlayerProfile = RdiSerializer.gson.fromJson(json, RdiPlayerProfile::class.java)
            (loginPacketListener as AccessServerLoginPacketListenerImpl).setGameProfile(
                GameProfile(
                    UUID.fromString(
                        rdiPlayerProfile.uuid
                    ), rdiPlayerProfile.name
                )
            )
            RdiPlayerProfileManager.addProfile(rdiPlayerProfile.uuid,rdiPlayerProfile)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }
}
