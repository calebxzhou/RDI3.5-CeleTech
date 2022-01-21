package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.command.PlayerArgCommand;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.model.ApiResponse;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import calebzhou.rdimc.celestech.utils.TextUtils;
import net.minecraft.server.network.ServerPlayerEntity;

public class InviteCommand extends PlayerArgCommand {
    public InviteCommand(String name, int permissionLevel) {
        super(name, permissionLevel,true);
    }

    @Override
    protected void onExecute(ServerPlayerEntity fromPlayer, ServerPlayerEntity invitedPlayer) {
        if(fromPlayer==invitedPlayer){
            TextUtils.sendChatMessage(fromPlayer,"您不可以邀请自己!");
            return;
        }
        ApiResponse response = HttpUtils.sendRequest("PUT", "island/" + fromPlayer.getUuidAsString(), "member=" + invitedPlayer.getUuidAsString());
        TextUtils.sendChatMessage(fromPlayer,"您邀请了"+invitedPlayer.getEntityName());
        TextUtils.sendChatMessage(fromPlayer,response);
        if(response.isSuccess()){
            TextUtils.sendChatMessage(invitedPlayer,fromPlayer.getEntityName()+"邀请您加入了他的空岛", MessageType.INFO);
            TextUtils.sendClickableContent(invitedPlayer,"点击[这里]可以前往他的空岛","/home");
        }

    }

}
