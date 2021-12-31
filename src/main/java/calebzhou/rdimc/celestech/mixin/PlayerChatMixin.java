package calebzhou.rdimc.celestech.mixin;

import calebzhou.rdimc.celestech.event.PlayerChatCallback;
import calebzhou.rdimc.celestech.event.PlayerDeathCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.server.filter.TextStream;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public class PlayerChatMixin {
    @Shadow
    private ServerPlayerEntity player;


    @Inject(method = "handleMessage(Lnet/minecraft/server/filter/TextStream$Message;)V",
    at = @At(value = "INVOKE",
            target = "net/minecraft/server/MinecraftServer.getPlayerManager ()Lnet/minecraft/server/PlayerManager;",
            shift = At.Shift.AFTER,
    ordinal = 0))
    private void handleMsg(TextStream.Message message, CallbackInfo i){
        ActionResult result = PlayerChatCallback.EVENT.invoker().call(player, message);
        if(result == ActionResult.FAIL)
            i.cancel();
    }
}
