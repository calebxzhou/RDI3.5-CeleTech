package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.RDICeleTech;
import calebzhou.rdimc.celestech.command.BaseCommand;
import calebzhou.rdimc.celestech.command.PlayerArgCommand;
import calebzhou.rdimc.celestech.constant.ColorConstants;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.utils.ServerUtils;
import calebzhou.rdimc.celestech.utils.TextUtils;
import calebzhou.rdimc.celestech.utils.ThreadPool;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;

import java.util.HashMap;
import java.util.TimerTask;
import java.util.UUID;

import static calebzhou.rdimc.celestech.RDICeleTech.tpaMap;
import static calebzhou.rdimc.celestech.utils.TextUtils.*;

public class TpaCommand extends PlayerArgCommand {

    public TpaCommand(String command, int permissionLevel) {
        super(command, permissionLevel);
    }

    @Override
    protected void onExecute(ServerPlayerEntity fromPlayer, ServerPlayerEntity toPlayer) {
        String fromPlayerId = fromPlayer.getUuidAsString();
        String toPlayerId = toPlayer.getUuidAsString();
        if(fromPlayerId.equals(toPlayerId)){
            sendChatMessage(fromPlayer,"禁止原地TP", MessageType.ERROR);
            return;
        }
        if(fromPlayer.experienceLevel<3){
            sendChatMessage(fromPlayer,"经验不足,您需要3级经验.", MessageType.ERROR);
            return;
        }

        if(tpaMap.containsKey(fromPlayer.getUuidAsString())){
            sendChatMessage(fromPlayer,"您已经发送过传送请求了", MessageType.ERROR);
            return ;
        }
        tpaMap.put(fromPlayerId,toPlayerId);
        sendChatMessage(fromPlayer,"已经发送传送请求，15秒后传送请求将失效。",MessageType.INFO);
        fromPlayer.experienceLevel-=3;
        sendChatMessage(toPlayer, ColorConstants.ORANGE+fromPlayer.getEntityName()+"想要传送到你的身边。");
        MutableText tpyes= getClickableContentComp(ColorConstants.BRIGHT_GREEN+"[接受]"+ ColorConstants.RESET,"/tpreq true_false_"+fromPlayerId," ");
        MutableText tpyes2= getClickableContentComp(ColorConstants.AQUA+"[以仅参观模式接受]"+ ColorConstants.RESET,"/tpreq true_true_"+fromPlayerId,"对方将没有破坏权限!");
        MutableText tpwait= getClickableContentComp(ColorConstants.GOLD+"[等我一下]"+ ColorConstants.RESET,"稍等"," ");
        MutableText tpdeny= getClickableContentComp(ColorConstants.RED+"[拒绝]"+ ColorConstants.RESET,"/tpreq false_false_"+fromPlayerId," ");
        sendChatMessage(toPlayer,tpyes.append(tpyes2).append(tpwait).append(tpdeny));
        ThreadPool.newThread(()->{
            try {
                Thread.sleep(15*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            tpaMap.remove(fromPlayerId);
        });
    }

}
