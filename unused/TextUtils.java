package calebzhou.rdi.core.server.utils;

import calebzhou.rdi.core.server.RdiCoreServer;
import calebzhou.rdi.core.server.constant.ColorConst;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.*;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Function;

public final class TextUtils {

    //发送可点击内容，类似tellraw
   /* public static void sendClickableContent(ServerPlayer player, String content, String commandTodo) {
        sendClickableContent(player,content,commandTodo,"点击以执行");
    }
    public static void sendClickableContent(Player player, String content, String commandTodo, String hoverContent) {
        sendChatMessage(player,getClickableContentComp(content,commandTodo,hoverContent));
    }*/
    /*public static MutableComponent getClickableContentComp(String content, String commandTodo, String hoverContent) {
        MutableComponent comp=Component.literal(content);
        return comp.withStyle(comp.getStyle()
                .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,commandTodo))
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Component.literal(hoverContent))));
    }*/
    //连接两个text
   /* public static MutableComponent concatTexts(MutableComponent... texts) {
        MutableComponent text=Component.literal("");
        for (MutableComponent mutableText : texts) {
            text.append(mutableText);
        }
        return  text;
    }
    public static MutableComponent concatTexts(String c1, Component c2) {
        return Component.literal(c1).append(c2);
    }

    //发送标题
    public static void sendTitle(ServerPlayer target,String title,TitleType type){
        ArrayList list = new ArrayList();
        list.add(target);
        try {
            sendTitle(list,Component.literal(title),type);
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }
    }
    public static void sendTitle(Collection<ServerPlayer> targets, Component title, TitleType titleType) throws CommandSyntaxException {
        Function<Component, Packet<?>> constructor=null;

        if(titleType==TitleType.TITLE)
            constructor = ClientboundSetTitleTextPacket::new;
        if(titleType == TitleType.SUBTITLE)
            constructor = ClientboundSetSubtitleTextPacket::new;

        CommandSourceStack source = RdiCoreServer.getServer().createCommandSourceStack();
        Iterator var5 = targets.iterator();

        while(var5.hasNext()) {
            ServerPlayer serverPlayerEntity = (ServerPlayer)var5.next();
            serverPlayerEntity.connection.send(constructor.apply(ComponentUtils.updateForEntity(source, title, serverPlayerEntity, 0)));
        }


    }*/
}
