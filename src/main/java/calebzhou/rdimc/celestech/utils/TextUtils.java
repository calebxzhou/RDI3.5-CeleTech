package calebzhou.rdimc.celestech.utils;

import calebzhou.rdimc.celestech.RDICeleTech;
import calebzhou.rdimc.celestech.constant.ColorConst;
import calebzhou.rdimc.celestech.constant.MessageType;
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
    public static final String ERROR_PREFIX = ColorConst.DARK_RED+ ColorConst.BOLD+
            "错误 >"+ ColorConst.RESET+ ColorConst.RED;
    public static final String SUCCESS_PREFIX = ColorConst.DARK_GREEN+ ColorConst.BOLD+
            "成功 >"+ ColorConst.RESET+ ColorConst.BRIGHT_GREEN;
    public static final String INFO_PREFIX = ColorConst.DARK_AQUA+ ColorConst.BOLD+
            "提示 >"+ ColorConst.RESET+ ColorConst.AQUA;
    //给玩家发送信息，在聊天框

    public static void sendPlayerMessage(Player player, Component textComponent, boolean actionBar) {
        if(player==null) return;
        player.displayClientMessage(textComponent, actionBar);
    }


    public static void sendChatMessage(Player player, MessageType messageType,String content ){
        switch (messageType){
            case ERROR -> sendChatMessage(player,ERROR_PREFIX+content);
            case INFO -> sendChatMessage(player,INFO_PREFIX+content);
            case SUCCESS -> sendChatMessage(player,SUCCESS_PREFIX+content);
        }

    }
    public static void sendChatMessage(Player player, String content) {
        sendChatMessage(player,Component.literal(content));
    }

    public static void sendChatMessage(CommandSourceStack source , String content){
        source.sendSuccess(Component.literal(content),false);
    }

    public static void sendChatMessage(Player player, Component textComponent) {
        sendPlayerMessage(player, textComponent, false);
    }

    //给玩家发送信息，在物品栏上方
    public static void sendActionMessage(Player player, String content) {
        sendActionMessage(player,Component.literal(content));
    }

    public static void sendActionMessage(Player player, Component textComponent) {
        sendPlayerMessage(player, textComponent, true);
    }

    //发送全局消息
    public static void sendGlobalChatMessage(PlayerList players, String content) {
        sendGlobalMessage(players, Component.literal(content), false);
    }
    public static void sendGlobalChatMessage(PlayerList  players, Component textComponent) {
        sendGlobalMessage(players, textComponent, false);
    }
    private static void sendGlobalMessage(PlayerList players, Component textComponent, boolean actionBar) {
        for (int i = 0; i < players.getPlayers().size(); ++i) {
            ServerPlayer player = players.getPlayers().get(i);
            sendPlayerMessage(player, textComponent, actionBar);
        }
    }

    //发送可点击内容，类似tellraw
    public static void sendClickableContent(ServerPlayer player, String content, String commandTodo) {
        sendClickableContent(player,content,commandTodo,"点击以执行");
    }
    public static void sendClickableContent(Player player, String content, String commandTodo, String hoverContent) {
        sendChatMessage(player,getClickableContentComp(content,commandTodo,hoverContent));
    }
    public static MutableComponent getClickableContentComp(String content, String commandTodo, String hoverContent) {
        MutableComponent comp=Component.literal(content);
        return comp.withStyle(comp.getStyle()
                .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,commandTodo))
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Component.literal(hoverContent))));
    }
    //连接两个text
    public static MutableComponent concatTexts(MutableComponent... texts) {
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

        CommandSourceStack source = RDICeleTech.getServer().createCommandSourceStack();
        Iterator var5 = targets.iterator();

        while(var5.hasNext()) {
            ServerPlayer serverPlayerEntity = (ServerPlayer)var5.next();
            serverPlayerEntity.connection.send(constructor.apply(ComponentUtils.updateForEntity(source, title, serverPlayerEntity, 0)));
        }


    }
}
