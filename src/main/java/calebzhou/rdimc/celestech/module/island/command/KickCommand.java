package calebzhou.rdimc.celestech.module.island.command;

import calebzhou.rdimc.celestech.command.ArgCommand;
import calebzhou.rdimc.celestech.command.BaseCommand;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.model.ApiResponse;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import calebzhou.rdimc.celestech.utils.PlayerUtils;
import calebzhou.rdimc.celestech.utils.TextUtils;
import net.minecraft.server.level.ServerPlayer;

public class KickCommand extends BaseCommand implements ArgCommand {
    public KickCommand(String name, int permissionLevel) {
        super(name, permissionLevel,true);
    }

    @Override
    public void onExecute(ServerPlayer fromPlayer, String kickPlayer) {
        if(fromPlayer.getScoreboardName().equalsIgnoreCase(kickPlayer)){
            TextUtils.sendChatMessage(fromPlayer,"您不可以踢出自己!!", MessageType.ERROR);
            return;
        }
        ApiResponse response = HttpUtils.sendRequestV2("DELETE", "v2/island_crew/" + fromPlayer.getStringUUID()+"/"+PlayerUtils.getPlayerByName(kickPlayer).getStringUUID());
        TextUtils.sendChatMessage(fromPlayer,response);
        TextUtils.sendChatMessage(PlayerUtils.getPlayerByName(kickPlayer),fromPlayer.getScoreboardName()+"删除了他的岛屿!");

    }


}
