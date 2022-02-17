package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.command.BaseCommand;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.utils.PlayerUtils;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameMode;

import static calebzhou.rdimc.celestech.RDICeleTech.tpaMap;
import static calebzhou.rdimc.celestech.utils.TextUtils.sendChatMessage;
import static calebzhou.rdimc.celestech.utils.TextUtils.sendClickableContent;

public class TpreqCommand extends BaseCommand {

    public TpreqCommand(String command, int permissionLevel) {
        super(command, permissionLevel,false);
    }

    @Override
    protected void onExecute(ServerPlayerEntity toPlayer, String arg) {
        try {
            String[] split = arg.split("_");
            boolean accept= Boolean.parseBoolean(split[0]);
            boolean visitOnly= Boolean.parseBoolean(split[1]);
            String fromPlayerId=split[2];
            ServerPlayerEntity fromPlayer=  PlayerUtils.getPlayerByUuid(fromPlayerId);
            if(toPlayer ==null){
                sendChatMessage(toPlayer,fromPlayer.getEntityName()+"不在线，您无法传送到对方。",MessageType.ERROR);
                return;
            }
            execute(toPlayer,accept,visitOnly,fromPlayer);
        } catch (ArrayIndexOutOfBoundsException e) {
            sendChatMessage(toPlayer,"命令格式错误!!",MessageType.ERROR);
        }
    }

    private void execute(ServerPlayerEntity toPlayer, boolean accept, boolean visitOnly, ServerPlayerEntity fromPlayer) {
        String fromPlayerId = fromPlayer.getUuidAsString();
        if(tpaMap.get(fromPlayerId) == null){
            sendChatMessage(toPlayer,"没有找到此请求。请确认1.对方是否在线2.是否已经接受了此请求");
            return;
        }
        if(!accept){
            sendChatMessage(fromPlayer,"对方拒绝了您的传送请求", MessageType.ERROR);
            tpaMap.remove(fromPlayerId);
            return;
        }
        if(toPlayer.experienceLevel<3){
            sendChatMessage(toPlayer,"经验不足,您需要3级经验.", MessageType.ERROR);
            return;
        }
        toPlayer.experienceLevel -= 3;

        sendChatMessage(toPlayer,"正在传送..");
        sendChatMessage(fromPlayer,"正在传送..");
        PlayerUtils.teleportPlayer(fromPlayer,toPlayer);
        if(visitOnly){
            fromPlayer.interactionManager.changeGameMode(GameMode.SPECTATOR);
            sendChatMessage(fromPlayer,"对方使用了'仅参观'模式接受了你的传送请求");
            sendClickableContent(fromPlayer,"若要恢复,点击这里.","/spawn");
        }

        tpaMap.remove(fromPlayerId);
    }
}
