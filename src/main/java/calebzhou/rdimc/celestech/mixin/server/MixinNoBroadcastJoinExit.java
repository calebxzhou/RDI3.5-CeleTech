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
    @Redirect(method ="Lnet/minecraft/server/network/ServerGamePacketListenerImpl;onDisconnect(Lnet/minecraft/network/chat/Component;)V",
            at=@At(value = "INVOKE",target = "Lnet/minecraft/server/players/PlayerList;broadcastMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/ChatType;Ljava/util/UUID;)V"))
    private void noBroadcastDisconnecting(PlayerList instance, Component message, ChatType type, UUID sender){
        //什么都不做
    }
}
@Mixin(PlayerList.class)
class NoJoin{
    //进入服务器不显示"XXX进入"
    @Redirect(method = "Lnet/minecraft/server/players/PlayerList;placeNewPlayer(Lnet/minecraft/network/Connection;Lnet/minecraft/server/level/ServerPlayer;)V",
    at=@At(value = "INVOKE",target = "Lnet/minecraft/server/players/PlayerList;broadcastMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/ChatType;Ljava/util/UUID;)V"))
    private void nojoinsay(PlayerList instance, Component message, ChatType type, UUID sender){
        //什么都不做
    }
}