package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.command.ArgCommand;
import calebzhou.rdimc.celestech.command.BaseCommand;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.model.ApiResponse;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import calebzhou.rdimc.celestech.utils.PlayerUtils;
import calebzhou.rdimc.celestech.utils.TextUtils;
import net.minecraft.server.level.ServerPlayer;

public class InviteCommand extends BaseCommand implements ArgCommand {
    public InviteCommand(String name, int permissionLevel) {
        super(name, permissionLevel,true);
    }

    @Override
    public void onExecute(ServerPlayer player, String invitedPlayer) {
        if(player.getScoreboardName().equalsIgnoreCase(invitedPlayer)){
            TextUtils.sendChatMessage(player,"您不可以邀请自己加入岛屿!",MessageType.ERROR);
            return;
        }
        ApiResponse response = HttpUtils.sendRequestV2("POST", "v2/island_crew/" + player.getStringUUID()+"/"+PlayerUtils.getPlayerByName(invitedPlayer).getStringUUID());
        TextUtils.sendChatMessage(player,"您邀请了"+invitedPlayer);
        TextUtils.sendChatMessage(player,response);
        if(response.isSuccess()){
            TextUtils.sendChatMessage(PlayerUtils.getPlayerByName(invitedPlayer),player.getScoreboardName()+"邀请您加入了他的岛屿", MessageType.INFO);
            TextUtils.sendClickableContent(PlayerUtils.getPlayerByName(invitedPlayer),"按下[H键]可以前往他的岛屿","/home");
        }

    }

}
