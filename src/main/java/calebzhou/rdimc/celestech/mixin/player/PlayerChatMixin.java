package calebzhou.rdimc.celestech.mixin.player;

import calebzhou.rdimc.celestech.RDICeleTech;
import calebzhou.rdimc.celestech.command.impl.ChatRangeCommand;
import calebzhou.rdimc.celestech.constant.ColorConstants;
import calebzhou.rdimc.celestech.event.PlayerChatCallback;
import calebzhou.rdimc.celestech.event.PlayerDeathCallback;
import calebzhou.rdimc.celestech.model.record.GenericRecord;
import calebzhou.rdimc.celestech.model.record.RecordType;
import calebzhou.rdimc.celestech.utils.*;
import com.mojang.brigadier.LiteralMessage;
import net.minecraft.client.option.ChatVisibility;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.MessageType;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
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
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class PlayerChatMixin {
    @Shadow
    public ServerPlayerEntity player;
    @Shadow @Final
    private MinecraftServer server;


    @Inject(method= "handleMessage(Lnet/minecraft/server/filter/TextStream$Message;)V",
            at=@At("HEAD"))
    private void recordMsg(TextStream.Message message, CallbackInfo ci) {
        String msg = message.getRaw();
        //上传消息
        GenericRecord cr = new GenericRecord(player.getUuidAsString(), RecordType.chat, player.getEntityName(), null, EncodingUtils.getUTF8StringFromGBKString(msg));
        HttpUtils.asyncSendObject(cr);
    }


    @Inject(method= "handleMessage(Lnet/minecraft/server/filter/TextStream$Message;)V",
            at=@At(value="INVOKE",target = "Lnet/minecraft/server/MinecraftServer;getPlayerManager()Lnet/minecraft/server/PlayerManager;",ordinal = 0), cancellable = true)
    private void handleMessage(TextStream.Message message, CallbackInfo ci) {
        ActionResult result = PlayerChatCallback.EVENT.invoker().call(player, message);
        if(result == ActionResult.FAIL) ci.cancel();
    }


    @Redirect(method = "handleMessage(Lnet/minecraft/server/filter/TextStream$Message;)V",
    at=@At(value="INVOKE",target = "Lnet/minecraft/server/PlayerManager;broadcast(Lnet/minecraft/text/Text;Ljava/util/function/Function;Lnet/minecraft/network/MessageType;Ljava/util/UUID;)V"))
    private void sendAll(PlayerManager instance, Text serverMessage, Function<ServerPlayerEntity, Text> playerMessageFactory, MessageType type, UUID sender){
        String pid = player.getUuidAsString();
        List<String> toSendList = ChatRangeCommand.chatRangeMap.get(pid);
        //岛内模式
        if(toSendList!=null){
            serverMessage = new LiteralText(ColorConstants.AQUA+"岛内 >").append(serverMessage);
            for(String toSend:toSendList){
                ServerPlayerEntity player = PlayerUtils.getPlayerByUuid(toSend);
                TextUtils.sendChatMessage(player,serverMessage);
            }
            //TextUtils.sendChatMessage(player,serverMessage);
        }else{
            instance.broadcast(serverMessage,playerMessageFactory,type,sender);
        }

    }

}
