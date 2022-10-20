package calebzhou.rdi.core.server.mixin.server;

import net.minecraft.network.chat.Component;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class mNoBroadcastJoinExit {
    //退出服务器不显示"XXX退出"
    @Redirect(method = "onDisconnect(Lnet/minecraft/network/chat/Component;)V",
            at=@At(value = "INVOKE",target = "Lnet/minecraft/server/players/PlayerList;broadcastSystemMessage(Lnet/minecraft/network/chat/Component;Z)V"))
    private void noBroadcastDisconnecting(PlayerList instance, Component message, boolean bl){
        //什么都不做
    }
}
@Mixin(PlayerList.class)
class NoJoin{
    //进入服务器不显示"XXX进入"
    @Redirect(method = "placeNewPlayer(Lnet/minecraft/network/Connection;Lnet/minecraft/server/level/ServerPlayer;)V",
    at=@At(value = "INVOKE",target =  "Lnet/minecraft/server/players/PlayerList;broadcastSystemMessage(Lnet/minecraft/network/chat/Component;Z)V"))
    private void nojoinsay(PlayerList instance, Component message, boolean bl){
        //什么都不做
    }
}
