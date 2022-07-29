package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.command.ArgCommand;
import calebzhou.rdimc.celestech.command.BaseCommand;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import calebzhou.rdimc.celestech.utils.PlayerUtils;
import calebzhou.rdimc.celestech.utils.TextUtils;
import net.minecraft.server.level.ServerPlayer;

import static calebzhou.rdimc.celestech.utils.TextUtils.sendChatMessage;

public class KickCommand extends BaseCommand implements ArgCommand {
    public KickCommand(String name, int permissionLevel) {
        super(name, permissionLevel,true);
    }

    @Override
    public void onExecute(ServerPlayer fromPlayer, String kickPlayer) {
        if(fromPlayer.getScoreboardName().equalsIgnoreCase(kickPlayer)){
            TextUtils.sendChatMessage(fromPlayer, MessageType.ERROR,"您不可以踢出自己!!");
            return;
        }
        String response = HttpUtils.sendRequest("delete", "island/crew/" + fromPlayer.getStringUUID()+"/"+PlayerUtils.getPlayerByName(kickPlayer).getStringUUID());
        if(response.equals("1")){
            TextUtils.sendChatMessage(PlayerUtils.getPlayerByName(kickPlayer),fromPlayer.getScoreboardName()+"删除了他的岛屿!");
            sendChatMessage(fromPlayer,MessageType.SUCCESS,"1");
        }


    }


}
