package calebzhou.rdi.core.server;

import calebzhou.rdi.core.server.mixin.AccessServerLoginPacketListenerImpl;
import calebzhou.rdi.core.server.model.RdiUser;
import calebzhou.rdi.core.server.model.ResultData;
import calebzhou.rdi.core.server.utils.RdiHttpClient;
import calebzhou.rdi.core.server.utils.RdiSerializer;
import calebzhou.rdi.core.server.utils.ThreadPool;
import com.google.gson.JsonSyntaxException;
import com.mojang.authlib.GameProfile;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.login.ServerboundHelloPacket;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;

import java.net.InetSocketAddress;
import java.util.UUID;

/**
 * Created by calebzhou on 2022-09-18,21:08.
 */
public class RdiLoginProtocol {
	public static void handleHello(Connection connection,ServerLoginPacketListenerImpl loginPacketListener, ServerboundHelloPacket helloPacket){
		try {
			String json = helloPacket.name();
			RdiCoreServer.LOGGER.info("收到登录请求：{}",json);

			if(!json.contains("{")){
				RdiCoreServer.LOGGER.info("此请求协议格式错误！");
				connection.disconnect(Component.literal("登录协议错误 格式错误1，请更新客户端！"));
				return;
			}
			RdiUser rdiUser = RdiSerializer.GSON.fromJson(json, RdiUser.class);
			((AccessServerLoginPacketListenerImpl) loginPacketListener).setGameProfile(new GameProfile(UUID.fromString(rdiUser.getUuid()), rdiUser.getName()));
			((AccessServerLoginPacketListenerImpl) loginPacketListener).setState(ServerLoginPacketListenerImpl.State.READY_TO_ACCEPT);
			RdiMemoryStorage.pidUserMap.put(rdiUser.getUuid(), rdiUser);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
