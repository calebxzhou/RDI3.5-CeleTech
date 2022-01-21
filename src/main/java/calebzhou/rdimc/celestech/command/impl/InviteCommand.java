package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.command.OneArgCommand;
import calebzhou.rdimc.celestech.model.ApiResponse;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import calebzhou.rdimc.celestech.utils.PlayerUtils;
import calebzhou.rdimc.celestech.utils.TextUtils;
import net.minecraft.server.network.ServerPlayerEntity;

public class InviteCommand extends OneArgCommand {
    public InviteCommand(String name, int permissionLevel) {
        super(name, permissionLevel,true);
    }

    @Override
    protected void onExecute(ServerPlayerEntity fromPlayer, String arg) {
        ServerPlayerEntity invitedPlayer = PlayerUtils.getPlayerByName(arg);
        ApiResponse response = HttpUtils.sendRequest("PUT", "island/" + fromPlayer.getUuidAsString(), "member=" + invitedPlayer.getUuidAsString());
        TextUtils.sendChatMessage(fromPlayer,response);
    }

}
