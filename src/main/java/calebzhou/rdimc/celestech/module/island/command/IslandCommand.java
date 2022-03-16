package calebzhou.rdimc.celestech.module.island.command;

import calebzhou.rdimc.celestech.command.BaseCommand;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.model.ApiResponse;
import calebzhou.rdimc.celestech.model.Island;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import net.minecraft.server.level.ServerPlayer;

import static calebzhou.rdimc.celestech.utils.TextUtils.sendChatMessage;

public class IslandCommand extends BaseCommand {
    public IslandCommand(String name, int permissionLevel) {
        super(name, permissionLevel,true);
    }

    @Override
    protected void onExecute(ServerPlayer player,String arg) {
        ApiResponse<Island> response = HttpUtils.sendRequestV2("GET","v2/island/"+player.getStringUUID(),"idType=pid");
        try {
            if(response.isSuccess()){
                sendChatMessage(player,"岛id："+response.getData(Island.class).getIslandId());
            }

        } catch (NullPointerException e) {
            sendChatMessage(player,"岛屿不存在!", MessageType.ERROR);
        }
    }
}
