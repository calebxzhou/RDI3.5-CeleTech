package calebzhou.rdimc.celestech.module.island.command;

import calebzhou.rdimc.celestech.command.BaseCommand;
import calebzhou.rdimc.celestech.model.ApiResponse;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import calebzhou.rdimc.celestech.utils.TextUtils;
import net.minecraft.server.network.ServerPlayerEntity;

public class QuitCommand extends BaseCommand {
    public QuitCommand(String name, int permissionLevel) {
        super(name, permissionLevel,true);
    }

    @Override
    protected void onExecute(ServerPlayerEntity player,String arg) {
        ApiResponse response = HttpUtils.sendRequestV2("DELETE", "island/" + player.getUuidAsString(), "isOwner=false");
        TextUtils.sendChatMessage(player,response);
    }
}
