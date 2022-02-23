package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.command.ArgCommand;
import calebzhou.rdimc.celestech.command.BaseCommand;
import calebzhou.rdimc.celestech.model.ApiResponse;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import calebzhou.rdimc.celestech.utils.TextUtils;
import net.minecraft.server.network.ServerPlayerEntity;

public class BindCommand extends BaseCommand implements ArgCommand {
    public BindCommand(String name, int permissionLevel) {
        super(name, permissionLevel, true);
    }

    @Override
    public void onExecute(ServerPlayerEntity player, String arg) {
        ApiResponse response = HttpUtils.sendRequestV2("POST", "v2/qbind/" + arg+"/"+player.getUuidAsString());
        TextUtils.sendChatMessage(player,response);
    }
}
