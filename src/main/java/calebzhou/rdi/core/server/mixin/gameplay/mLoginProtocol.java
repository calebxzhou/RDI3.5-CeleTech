package calebzhou.rdi.core.server.mixin.gameplay;


import calebzhou.rdi.core.server.misc.RdiLoginProtocol;
import calebzhou.rdi.core.server.mixin.AccessServerLoginPacketListenerImpl;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.login.ServerboundHelloPacket;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;
import net.minecraft.util.SignatureValidator;
import net.minecraft.world.entity.player.ProfilePublicKey;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import java.util.UUID;

@Mixin(ServerboundHelloPacket.class)
public class mLoginProtocol {
    private static final int nameLen=128;//给uuid和@符号腾出来空间
    @ModifyConstant(method = "<init>(Lnet/minecraft/network/FriendlyByteBuf;)V",constant =
    @Constant(intValue = 16))
    private static int changeNameLength(int constant){
        return nameLen;
    }

}
@Mixin(ServerLoginPacketListenerImpl.class)
abstract
class mNewLoginProtocol {

	@Shadow @Final public Connection connection;

	//格式：姓名@uuid
	@Overwrite
	public void handleHello(ServerboundHelloPacket serverboundHelloPacket) {
		boolean b = RdiLoginProtocol.handleHello(connection, (ServerLoginPacketListenerImpl) (Object) this, serverboundHelloPacket);
		if(b){
			((AccessServerLoginPacketListenerImpl) this).setState(ServerLoginPacketListenerImpl.State.READY_TO_ACCEPT);
		}else{
			connection.disconnect(Component.literal("登录协议错误 格式错误2，请更新客户端！"));
		}
	}

	@Overwrite
	@Nullable
	private static ProfilePublicKey validatePublicKey(
			@Nullable ProfilePublicKey.Data profilePublicKeyData, UUID profileId, SignatureValidator signatureValidator, boolean enforceSecureProfile
	) {
				return null;
	}
}
