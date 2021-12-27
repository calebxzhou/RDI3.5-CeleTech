package calebzhou.rdimc.celestech.utils;

import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.*;

import java.awt.*;
import java.util.UUID;

public final class TextUtils {

    public static void sendPlayerMessage(PlayerEntity player, Text textComponent, boolean actionBar) {
        player.sendMessage(textComponent, actionBar);
    }

    //给玩家发送信息，在聊天框
    public static void sendChatMessage(PlayerEntity player, String content) {
        sendChatMessage(player,new LiteralText(content));
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
    public static MutableText concatTwoComponents(MutableText c1, MutableText c2) {
        return c1.append(c2);
    }
    public static MutableText concatTwoComponents(String c1, Text c2) {
        return new LiteralText(c1).append(c2);
    }

}
