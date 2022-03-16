package calebzhou.rdimc.celestech.mixin.server;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.UUID;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.players.PlayerList;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class MixinNoBroadcastJoinExit {
    //退出服务器不显示"XXX退出"
    @Redirect(method ="Lnet/minecraft/server/network/ServerPlayNetworkHandler;onDisconnected(Lnet/minecraft/text/Text;)V",
            at=@At(value = "INVOKE",target = "Lnet/minecraft/server/PlayerManager;broadcast(Lnet/minecraft/text/Text;Lnet/minecraft/network/MessageType;Ljava/util/UUID;)V"))
    private void noBroadcastDisconnecting(PlayerList instance, Component message, ChatType type, UUID sender){
        //System.out.println(message.getString());
    }
}
@Mixin(PlayerList.class)
class NoJoin{
    @Redirect(method = "Lnet/minecraft/server/PlayerManager;onPlayerConnect(Lnet/minecraft/network/ClientConnection;Lnet/minecraft/server/network/ServerPlayerEntity;)V",
    at=@At(value = "INVOKE",target = "Lnet/minecraft/server/PlayerManager;broadcast(Lnet/minecraft/text/Text;Lnet/minecraft/network/MessageType;Ljava/util/UUID;)V"))
    private void nojoinsay(PlayerList instance, Component message, ChatType type, UUID sender){

    }
}