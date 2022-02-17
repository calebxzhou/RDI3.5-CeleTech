package calebzhou.rdimc.celestech.module.island.command;

import calebzhou.rdimc.celestech.command.BaseCommand;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.model.ApiResponse;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import calebzhou.rdimc.celestech.utils.PlayerUtils;
import calebzhou.rdimc.celestech.utils.TextUtils;
import net.minecraft.server.network.ServerPlayerEntity;

public class KickCommand extends BaseCommand {
    public KickCommand(String name, int permissionLevel) {
        super(name, permissionLevel,true);
    }

    @Override
    protected void onExecute(ServerPlayerEntity fromPlayer,String kickPlayer) {
        if(fromPlayer.getEntityName().equalsIgnoreCase(kickPlayer)){
            TextUtils.sendChatMessage(fromPlayer,"您不可以踢出自己!!", MessageType.ERROR);
            return;
        }
        ApiResponse response = HttpUtils.sendRequestV2("DELETE", "v2/island/" + fromPlayer.getUuidAsString(), "isOwner=true","member=" + PlayerUtils.getPlayerByName(kickPlayer).getUuidAsString());
        TextUtils.sendChatMessage(fromPlayer,response);
        TextUtils.sendChatMessage(PlayerUtils.getPlayerByName(kickPlayer),fromPlayer.getEntityName()+"删除了他的空岛!");

    }


}
