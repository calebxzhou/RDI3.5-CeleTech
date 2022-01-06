package calebzhou.rdimc.celestech.mixin;

import calebzhou.rdimc.celestech.event.PlayerConnectServerCallback;
import calebzhou.rdimc.celestech.event.PlayerDisconnectServerCallback;
import calebzhou.rdimc.celestech.event.RegisterCommandsCallback;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.MessageType;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(PlayerManager.class)
public abstract class PlayerConnectServerMixin {

    @Shadow @Final protected int maxPlayers;

    @Inject(at = @At("TAIL"),method = "onPlayerConnect(Lnet/minecraft/network/ClientConnection;Lnet/minecraft/server/network/ServerPlayerEntity;)V")
    private void mixConnectEvent(ClientConnection connection, ServerPlayerEntity player,CallbackInfo callbackInfo){
        ActionResult result = PlayerConnectServerCallback.EVENT.invoker().connect(connection, player);
        if(result == ActionResult.FAIL)
            callbackInfo.cancel();
    }

    @Inject(at = @At("HEAD"),method = "remove(Lnet/minecraft/server/network/ServerPlayerEntity;)V")
    private void mixDis(ServerPlayerEntity player,CallbackInfo callbackInfo){
        ActionResult result = PlayerDisconnectServerCallback.EVENT.invoker().connect(player);
        if(result == ActionResult.FAIL)
            callbackInfo.cancel();
    }
}
