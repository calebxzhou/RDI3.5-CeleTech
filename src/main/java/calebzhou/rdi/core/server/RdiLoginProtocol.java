package calebzhou.rdi.core.server;

import calebzhou.rdi.core.server.mixin.AccessServerLoginPacketListenerImpl;
import calebzhou.rdi.core.server.model.RdiUser;
import calebzhou.rdi.core.server.model.ResultData;
import calebzhou.rdi.core.server.utils.RdiHttpClient;
import calebzhou.rdi.core.server.utils.RdiSerializer;
import calebzhou.rdi.core.server.utils.ThreadPool;
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
		String json = helloPacket.name();
		RdiCoreServer.LOGGER.info("收到登录请求：{}",json);

		if(!json.contains("{")){
			connection.disconnect(Component.literal("登录协议错误 格式错误1，请更新客户端！"));
			return;
		}
		RdiUser rdiUser = RdiSerializer.GSON.fromJson(json, RdiUser.class);
		((AccessServerLoginPacketListenerImpl) loginPacketListener).setGameProfile(new GameProfile(UUID.fromString(rdiUser.getUuid()), rdiUser.getName()));
		((AccessServerLoginPacketListenerImpl) loginPacketListener).setState(ServerLoginPacketListenerImpl.State.READY_TO_ACCEPT);
		ThreadPool.newThread(()->{
			ResultData resultData = RdiHttpClient.sendRequest("get", "/v37/account/isreg/" + rdiUser.getUuid());
			if(resultData.isSuccess()){
				if (Boolean.parseBoolean(String.valueOf(resultData.getData()))){
					ResultData loginData = RdiHttpClient.sendRequest("get", "/v37/account/login/" + rdiUser.getUuid(), Pair.of("pwd", rdiUser.getPwd()));
					if (!loginData.isSuccess()) {
						connection.disconnect(Component.literal("RDI密码错误"));
					}
				}
			}
		});
	}
}
