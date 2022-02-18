package calebzhou.rdimc.celestech.module.island.command;

import calebzhou.rdimc.celestech.command.ArgCommand;
import calebzhou.rdimc.celestech.command.BaseCommand;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.model.ApiResponse;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import calebzhou.rdimc.celestech.utils.PlayerUtils;
import calebzhou.rdimc.celestech.utils.ServerUtils;
import calebzhou.rdimc.celestech.utils.TextUtils;
import net.minecraft.server.network.ServerPlayerEntity;

public class InviteCommand extends BaseCommand implements ArgCommand {
    public InviteCommand(String name, int permissionLevel) {
        super(name, permissionLevel,true);
    }

    @Override
    public void onExecute(ServerPlayerEntity player, String invitedPlayer) {
        if(player.getEntityName().equalsIgnoreCase(invitedPlayer)){
            TextUtils.sendChatMessage(player,"您不可以邀请自己加入岛屿!",MessageType.ERROR);
            return;
        }
        ApiResponse response = HttpUtils.sendRequestV2("POST", "v2/island_crew/" + player.getUuidAsString()+"/"+PlayerUtils.getPlayerByName(invitedPlayer).getUuidAsString());
        TextUtils.sendChatMessage(player,"您邀请了"+invitedPlayer);
        TextUtils.sendChatMessage(player,response);
        if(response.isSuccess()){
            TextUtils.sendChatMessage(PlayerUtils.getPlayerByName(invitedPlayer),player.getEntityName()+"邀请您加入了他的岛屿", MessageType.INFO);
            TextUtils.sendClickableContent(PlayerUtils.getPlayerByName(invitedPlayer),"按下[H键]可以前往他的岛屿","/home");
        }

    }

}
