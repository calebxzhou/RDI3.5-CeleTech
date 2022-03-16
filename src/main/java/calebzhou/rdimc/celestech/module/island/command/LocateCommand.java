package calebzhou.rdimc.celestech.module.island.command;

import calebzhou.rdimc.celestech.command.BaseCommand;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.model.ApiResponse;
import calebzhou.rdimc.celestech.model.CoordLocation;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import calebzhou.rdimc.celestech.utils.PlayerUtils;
import net.minecraft.server.level.ServerPlayer;

import static calebzhou.rdimc.celestech.utils.TextUtils.sendChatMessage;

public class LocateCommand extends BaseCommand {
    public LocateCommand(String name, int permissionLevel) {
        super(name, permissionLevel,true);
    }

    @Override
    protected void onExecute(ServerPlayer player,String arg) {
        if(PlayerUtils.getDimensionName(player).equals("minecraft:the_nether")){
            sendChatMessage(player,"您只能在自己的岛屿上设置传送点.", MessageType.ERROR);
            return;
        }
        ApiResponse response = HttpUtils.sendRequestV2("PUT","v2/island/"+player.getStringUUID(),"location="+CoordLocation.fromPlayer(player).toString());

        sendChatMessage(player,response);
    }

}
