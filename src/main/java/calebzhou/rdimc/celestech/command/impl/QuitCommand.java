package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.command.NoArgCommand;
import calebzhou.rdimc.celestech.model.ApiResponse;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import calebzhou.rdimc.celestech.utils.TextUtils;
import net.minecraft.server.network.ServerPlayerEntity;

public class QuitCommand extends NoArgCommand {
    public QuitCommand(String name, int permissionLevel) {
        super(name, permissionLevel,true);
    }

    @Override
    protected void onExecute(ServerPlayerEntity fromPlayer) {
        ApiResponse response = HttpUtils.sendRequest("DELETE", "island/" + fromPlayer.getUuidAsString(), "isOwner=false");
        TextUtils.sendChatMessage(fromPlayer,response);
    }
}
