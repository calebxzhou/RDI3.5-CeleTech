package calebzhou.rdimc.celestech.mixin.server;

import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayerGameMode.class)
public class MixinNoSendGameMode {
    @Redirect(
            method = "Lnet/minecraft/server/network/ServerPlayerInteractionManager;setGameMode(Lnet/minecraft/world/GameMode;Lnet/minecraft/world/GameMode;)V"
            ,at = @At(value = "INVOKE",target = "Lnet/minecraft/server/PlayerManager;sendToAll(Lnet/minecraft/network/Packet;)V"))
    private void no(PlayerList instance, Packet<?> packet){}

}
