package calebzhou.rdimc.celestech.mixin;

import calebzhou.rdimc.celestech.RDICeleTech;
import calebzhou.rdimc.celestech.event.PlayerChatCallback;
import calebzhou.rdimc.celestech.event.PlayerDeathCallback;
import calebzhou.rdimc.celestech.utils.TextUtils;
import net.minecraft.client.option.ChatVisibility;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.MessageType;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.filter.TextStream;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class PlayerChatMixin {
    @Shadow
    public ServerPlayerEntity player;
    @Shadow @Final
    private MinecraftServer server;
    private int messageCooldown;

    /**
     * @author
     */
    @Inject(method="Lnet/minecraft/server/network/ServerPlayNetworkHandler;handleMessage(Lnet/minecraft/server/filter/TextStream$Message;)V",
            at=@At(value="INVOKE",target = "Lnet/minecraft/server/MinecraftServer;getPlayerManager()Lnet/minecraft/server/PlayerManager;"))
    private void handleMessage(TextStream.Message message, CallbackInfo ci) {

                //String string2 = message.getFiltered();
                ActionResult result = PlayerChatCallback.EVENT.invoker().call(player, message);
                if(result == ActionResult.FAIL) return;
                /*this.server.getPlayerManager().broadcast(text2, (player) -> {
                    return this.player.shouldFilterMessagesSentTo(player) ? text : text2;
                }, MessageType.CHAT, this.player.getUuid());*/



    }
}
