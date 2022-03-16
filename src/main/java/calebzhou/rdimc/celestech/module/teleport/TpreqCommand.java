package calebzhou.rdimc.celestech.module.teleport;

import calebzhou.rdimc.celestech.command.ArgCommand;
import calebzhou.rdimc.celestech.command.BaseCommand;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.utils.PlayerUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;

import static calebzhou.rdimc.celestech.RDICeleTech.tpaMap;
import static calebzhou.rdimc.celestech.utils.TextUtils.sendChatMessage;
import static calebzhou.rdimc.celestech.utils.TextUtils.sendClickableContent;

public class TpreqCommand extends BaseCommand implements ArgCommand {

    public TpreqCommand(String command, int permissionLevel) {
        super(command, permissionLevel,false);
    }

    @Override
    public void onExecute(ServerPlayer toPlayer, String arg) {
        try {
            String[] split = arg.split("_");
            boolean accept= Boolean.parseBoolean(split[0]);
            boolean visitOnly= Boolean.parseBoolean(split[1]);
            String fromPlayerId=split[2];
            ServerPlayer fromPlayer=  PlayerUtils.getPlayerByUuid(fromPlayerId);
            if(toPlayer ==null){
                sendChatMessage(toPlayer,fromPlayer.getScoreboardName()+"不在线，您无法传送到对方。",MessageType.ERROR);
                return;
            }
            execute(toPlayer,accept,visitOnly,fromPlayer);
        } catch (ArrayIndexOutOfBoundsException e) {
            sendChatMessage(toPlayer,"命令格式错误!!",MessageType.ERROR);
        }
    }

    private void execute(ServerPlayer toPlayer, boolean accept, boolean visitOnly, ServerPlayer fromPlayer) {
        String fromPlayerId = fromPlayer.getStringUUID();
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
            fromPlayer.gameMode.changeGameModeForPlayer(GameType.SPECTATOR);
            sendChatMessage(fromPlayer,"对方使用了'仅参观'模式接受了你的传送请求");
            sendClickableContent(fromPlayer,"若要恢复,点击这里.","/spawn");
        }

        tpaMap.remove(fromPlayerId);
    }
}
