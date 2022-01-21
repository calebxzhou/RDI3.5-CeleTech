package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.command.OneArgCommand;
import calebzhou.rdimc.celestech.command.PlayerArgCommand;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.model.ApiResponse;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import calebzhou.rdimc.celestech.utils.PlayerUtils;
import calebzhou.rdimc.celestech.utils.TextUtils;
import net.minecraft.server.network.ServerPlayerEntity;

public class KickCommand extends PlayerArgCommand {
    public KickCommand(String name, int permissionLevel) {
        super(name, permissionLevel,true);
    }

    @Override
    protected void onExecute(ServerPlayerEntity fromPlayer, ServerPlayerEntity kickPlayer) {
        if(fromPlayer==kickPlayer){
            TextUtils.sendChatMessage(fromPlayer,"您不可以踢出自己!!", MessageType.ERROR);
            return;
        }
        ApiResponse response = HttpUtils.sendRequest("DELETE", "island/" + fromPlayer.getUuidAsString(), "isOwner=true","member=" + kickPlayer.getUuidAsString());
        TextUtils.sendChatMessage(fromPlayer,response);
        TextUtils.sendChatMessage(kickPlayer,fromPlayer.getEntityName()+"删除了他的空岛!");

    }


}
