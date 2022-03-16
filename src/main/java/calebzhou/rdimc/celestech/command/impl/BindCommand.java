package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.command.ArgCommand;
import calebzhou.rdimc.celestech.command.BaseCommand;
import calebzhou.rdimc.celestech.model.ApiResponse;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import calebzhou.rdimc.celestech.utils.TextUtils;
import net.minecraft.server.level.ServerPlayer;

public class BindCommand extends BaseCommand implements ArgCommand {
    public BindCommand(String name, int permissionLevel) {
        super(name, permissionLevel, true);
    }

    @Override
    public void onExecute(ServerPlayer player, String arg) {
        ApiResponse response = HttpUtils.sendRequestV2("POST", "v2/qbind/" + arg+"/"+player.getStringUUID());
        TextUtils.sendChatMessage(player,response);
    }
}
