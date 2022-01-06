package calebzhou.rdimc.celestech.utils;

import calebzhou.rdimc.celestech.RDICeleTech;
import calebzhou.rdimc.celestech.constant.ColorConstants;
import calebzhou.rdimc.celestech.constant.MessageType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.SubtitleS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.*;

import java.util.*;
import java.util.function.Function;

public final class TextUtils {
    public static final String ERROR_PREFIX = ColorConstants.DARK_RED+ColorConstants.BOLD+
            "错误 >"+ColorConstants.RESET+ColorConstants.RED;
    public static final String SUCCESS_PREFIX = ColorConstants.DARK_GREEN+ColorConstants.BOLD+
            "成功 >"+ColorConstants.RESET+ColorConstants.BRIGHT_GREEN;
    public static final String INFO_PREFIX = ColorConstants.DARK_AQUA+ColorConstants.BOLD+
            "提示 >"+ColorConstants.RESET+ColorConstants.AQUA;
    public static void sendPlayerMessage(PlayerEntity player, Text textComponent, boolean actionBar) {
        if(player==null) return;
        player.sendMessage(textComponent, actionBar);
    }
    public static void sendChatMessage(PlayerEntity player, String content, MessageType messageType){
        switch (messageType){
            case ERROR -> sendChatMessage(player,ERROR_PREFIX+content);
            case INFO -> sendChatMessage(player,INFO_PREFIX+content);
            case SUCCESS -> sendChatMessage(player,SUCCESS_PREFIX+content);
        }

    }
    //给玩家发送信息，在聊天框
    public static void sendChatMessage(PlayerEntity player, String content) {
        sendChatMessage(player,new LiteralText(content));
    }
    public static void sendChatMessage(ServerCommandSource source , String content){
        source.sendFeedback(new LiteralText(content),false);
    }

    public static void sendChatMessage(PlayerEntity player, Text textComponent) {
        sendPlayerMessage(player, textComponent, false);
    }

    //给玩家发送信息，在物品栏上方
    public static void sendActionMessage(PlayerEntity player, String content) {
        sendActionMessage(player,new LiteralText(content));
    }

    public static void sendActionMessage(PlayerEntity player, Text textComponent) {
        sendPlayerMessage(player, textComponent, true);
    }

    //发送全局消息
    public static void sendGlobalChatMessage(PlayerManager players, String content) {
        sendGlobalMessage(players, new LiteralText(content), false);
    }
    public static void sendGlobalChatMessage(PlayerManager  players, Text textComponent) {
        sendGlobalMessage(players, textComponent, false);
    }
    private static void sendGlobalMessage(PlayerManager players, Text textComponent, boolean actionBar) {
        for (int i = 0; i < players.getPlayerList().size(); ++i) {
            ServerPlayerEntity player = players.getPlayerList().get(i);
            sendPlayerMessage(player, textComponent, actionBar);
        }
    }

    //发送可点击内容，类似tellraw
    public static void sendClickableContent(ServerPlayerEntity player, String content, String commandTodo) {
        sendClickableContent(player,content,commandTodo,"点击以执行");
    }
    public static void sendClickableContent(PlayerEntity player, String content, String commandTodo, String hoverContent) {
        sendChatMessage(player,getClickableContentComp(content,commandTodo,hoverContent));
    }
    public static MutableText getClickableContentComp(String content, String commandTodo, String hoverContent) {
        MutableText comp=new LiteralText(content);
        return comp.fillStyle(comp.getStyle()
                .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,commandTodo))
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new LiteralText(hoverContent))));
    }
    //连接两个text
    public static MutableText concatTexts(MutableText... texts) {
        MutableText text=new LiteralText("");
        for (MutableText mutableText : texts) {
            text.append(mutableText);
        }
        return  text;
    }
    public static MutableText concatTexts(String c1, Text c2) {
        return new LiteralText(c1).append(c2);
    }

    //发送标题
    public static void sendTitle(ServerPlayerEntity target,String title){
        ArrayList list = new ArrayList();
        list.add(target);
        try {
            sendTitle(list,new LiteralText(title),TitleType.TITLE);
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }
    }
    public static void sendTitle(Collection<ServerPlayerEntity> targets, Text title, TitleType titleType) throws CommandSyntaxException {
        Function<Text, Packet<?>> constructor=null;

        if(titleType==TitleType.TITLE)
            constructor = TitleS2CPacket::new;
        if(titleType == TitleType.SUBTITLE)
            constructor = SubtitleS2CPacket::new;

        ServerCommandSource source = RDICeleTech.getServer().getCommandSource();
        Iterator var5 = targets.iterator();

        while(var5.hasNext()) {
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)var5.next();
            serverPlayerEntity.networkHandler.sendPacket(constructor.apply(Texts.parse(source, title, serverPlayerEntity, 0)));
        }


    }
}
enum TitleType{
    TITLE,SUBTITLE
}