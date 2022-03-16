package calebzhou.rdimc.celestech.module.teleport;

import calebzhou.rdimc.celestech.command.ArgCommand;
import calebzhou.rdimc.celestech.command.BaseCommand;
import calebzhou.rdimc.celestech.constant.ColorConstants;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.utils.PlayerUtils;
import calebzhou.rdimc.celestech.utils.ThreadPool;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;

import static calebzhou.rdimc.celestech.RDICeleTech.tpaMap;
import static calebzhou.rdimc.celestech.utils.TextUtils.getClickableContentComp;
import static calebzhou.rdimc.celestech.utils.TextUtils.sendChatMessage;

public class TpaCommand extends BaseCommand implements ArgCommand {

    public TpaCommand(String command, int permissionLevel) {
        super(command, permissionLevel,false);
    }

    @Override
    public void onExecute(ServerPlayer fromPlayer, String toPlayer) {
        String fromPlayerId = fromPlayer.getStringUUID();
        ServerPlayer toPlayero = PlayerUtils.getPlayerByName(toPlayer);
        String toPlayerId = toPlayero.getStringUUID();
        if(fromPlayerId.equals(toPlayerId)){
            sendChatMessage(fromPlayer,"禁止原地TP", MessageType.ERROR);
            return;
        }
        if(fromPlayer.experienceLevel<3){
            sendChatMessage(fromPlayer,"经验不足,您需要3级经验.", MessageType.ERROR);
            return;
        }

        if(tpaMap.containsKey(fromPlayer.getStringUUID())){
            sendChatMessage(fromPlayer,"您已经发送过传送请求了", MessageType.ERROR);
            return ;
        }
        tpaMap.put(fromPlayerId,toPlayerId);
        sendChatMessage(fromPlayer,"已经发送传送请求，15秒后传送请求将失效。",MessageType.INFO);
        fromPlayer.experienceLevel-=3;
        sendChatMessage(toPlayero, ColorConstants.ORANGE+fromPlayer.getScoreboardName()+"想要传送到你的身边。");
        MutableComponent tpyes= getClickableContentComp(ColorConstants.BRIGHT_GREEN+"[接受]"+ ColorConstants.RESET,"/tpreq true_false_"+fromPlayerId," ");
        MutableComponent tpyes2= getClickableContentComp(ColorConstants.AQUA+"[以仅参观模式接受]"+ ColorConstants.RESET,"/tpreq true_true_"+fromPlayerId,"对方将没有破坏权限!");
        MutableComponent tpwait= getClickableContentComp(ColorConstants.GOLD+"[等我一下]"+ ColorConstants.RESET,"稍等"," ");
        MutableComponent tpdeny= getClickableContentComp(ColorConstants.RED+"[拒绝]"+ ColorConstants.RESET,"/tpreq false_false_"+fromPlayerId," ");
        sendChatMessage(toPlayero,tpyes.append(tpyes2).append(tpwait).append(tpdeny));
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
