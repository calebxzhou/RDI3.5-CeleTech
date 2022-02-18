package calebzhou.rdimc.celestech.module.island.command;

import calebzhou.rdimc.celestech.command.BaseCommand;
import calebzhou.rdimc.celestech.model.ApiResponse;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import calebzhou.rdimc.celestech.utils.PlayerUtils;
import calebzhou.rdimc.celestech.utils.TextUtils;
import net.minecraft.server.network.ServerPlayerEntity;

public class QuitCommand extends BaseCommand {
    public QuitCommand(String name, int permissionLevel) {
        super(name, permissionLevel,true);
    }

    @Override
    protected void onExecute(ServerPlayerEntity player,String arg) {
        String ownerPid = PlayerUtils.getPlayerByName(arg).getUuidAsString();
        ApiResponse response = HttpUtils.sendRequestV2("DELETE", "v2/island_crew/" + ownerPid+"/"+player.getUuidAsString());
        TextUtils.sendChatMessage(player,response);
    }
}
