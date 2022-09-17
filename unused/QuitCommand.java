package calebzhou.rdi.celestech.command.impl;

import calebzhou.rdi.celestech.command.BaseCommand;
import calebzhou.rdi.celestech.model.ApiResponse;
import calebzhou.rdi.celestech.utils.HttpUtils;
import calebzhou.rdi.celestech.utils.PlayerUtils;
import calebzhou.rdi.celestech.utils.TextUtils;
import net.minecraft.server.level.ServerPlayer;

public class QuitCommand extends BaseCommand {
    public QuitCommand(String name, int permissionLevel) {
        super(name, permissionLevel,true);
    }

    @Override
    protected void onExecute(ServerPlayer player,String arg) {
        String ownerPid = PlayerUtils.getPlayerByName(arg).getStringUUID();
        ApiResponse response = HttpUtils.sendRequestV2("DELETE", "v2/island_crew/" + ownerPid+"/"+player.getStringUUID());
        PlayerUtils.sendChatMessage(player,response);
    }
}
