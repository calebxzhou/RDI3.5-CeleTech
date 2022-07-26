package calebzhou.rdimc.celestech.mixin.player;

import calebzhou.rdimc.celestech.event.PlayerConnectServerCallback;
import calebzhou.rdimc.celestech.event.PlayerDisconnectServerCallback;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.InteractionResult;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerList.class)
public abstract class PlayerConnectServerMixin {

    @Shadow @Final protected int maxPlayers;

    @Inject(at = @At("TAIL"),method = "placeNewPlayer(Lnet/minecraft/network/Connection;Lnet/minecraft/server/level/ServerPlayer;)V", cancellable = true)
    private void mixConnectEvent(Connection connection, ServerPlayer player,CallbackInfo callbackInfo){
        InteractionResult result = PlayerConnectServerCallback.EVENT.invoker().connect(connection, player);
        if(result == InteractionResult.FAIL)
            callbackInfo.cancel();
    }

    @Inject(at = @At("HEAD"),method = "remove(Lnet/minecraft/server/level/ServerPlayer;)V", cancellable = true)
    private void mixDis(ServerPlayer player,CallbackInfo callbackInfo){
        InteractionResult result = PlayerDisconnectServerCallback.EVENT.invoker().connect(player);
        if(result == InteractionResult.FAIL)
            callbackInfo.cancel();
    }
}
