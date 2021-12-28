package calebzhou.rdimc.celestech.mixin;

import calebzhou.rdimc.celestech.event.PlayerConnectServerCallback;
import calebzhou.rdimc.celestech.event.RegisterCommandsCallback;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public class PlayerConnectServerMixin {

    @Inject(at = @At("TAIL"),method = "onPlayerConnect(Lnet/minecraft/network/ClientConnection;Lnet/minecraft/server/network/ServerPlayerEntity;)V")
    private void mix(ClientConnection connection, ServerPlayerEntity player,CallbackInfo callbackInfo){
        ActionResult result = PlayerConnectServerCallback.EVENT.invoker().connect(connection, player);
        if(result == ActionResult.FAIL)
            callbackInfo.cancel();
    }
}
