package calebzhou.rdimc.celestech.mixin;

import net.minecraft.network.MessageType;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.UUID;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkMixin {
    //退出服务器不显示"XXX退出"
    @Redirect(method ="Lnet/minecraft/server/network/ServerPlayNetworkHandler;onDisconnected(Lnet/minecraft/text/Text;)V",
            at=@At(value = "INVOKE",target = "Lnet/minecraft/server/PlayerManager;broadcast(Lnet/minecraft/text/Text;Lnet/minecraft/network/MessageType;Ljava/util/UUID;)V"))
    private void noBroadcastDisconnecting(PlayerManager instance, Text message, MessageType type, UUID sender){
        System.out.println(message.getString());
    }
}
