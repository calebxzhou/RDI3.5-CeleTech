package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.command.ArgCommand;
import calebzhou.rdimc.celestech.command.BaseCommand;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import calebzhou.rdimc.celestech.utils.PlayerUtils;
import calebzhou.rdimc.celestech.utils.TextUtils;
import net.minecraft.server.level.ServerPlayer;

import static calebzhou.rdimc.celestech.utils.TextUtils.sendChatMessage;

public class InviteCommand extends BaseCommand implements ArgCommand {
    public InviteCommand(String name, int permissionLevel) {
        super(name, permissionLevel,true);
    }

    @Override
    public void onExecute(ServerPlayer player, String invitedPlayer) {
        if(player.getScoreboardName().equalsIgnoreCase(invitedPlayer)){
            TextUtils.sendChatMessage(player,MessageType.ERROR,"您不可以邀请自己加入岛屿!");
            return;
        }
        String response = HttpUtils.sendRequest("post", "island/crew/" + player.getStringUUID()+"/"+PlayerUtils.getPlayerByName(invitedPlayer).getStringUUID());
        TextUtils.sendChatMessage(player,"您邀请了"+invitedPlayer);
        if(response.equals("1")){
            TextUtils.sendChatMessage(PlayerUtils.getPlayerByName(invitedPlayer),MessageType.INFO,player.getScoreboardName()+"邀请您加入了他的岛屿");
            TextUtils.sendClickableContent(PlayerUtils.getPlayerByName(invitedPlayer),"按下[H键]可以前往他的岛屿","/home");
            sendChatMessage(player,MessageType.SUCCESS,"1");
        }

    }

}
