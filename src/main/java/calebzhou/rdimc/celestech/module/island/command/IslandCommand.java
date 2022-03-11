package calebzhou.rdimc.celestech.module.island.command;

import calebzhou.rdimc.celestech.command.BaseCommand;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.model.ApiResponse;
import calebzhou.rdimc.celestech.model.Island;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import net.minecraft.server.network.ServerPlayerEntity;

import static calebzhou.rdimc.celestech.utils.TextUtils.sendChatMessage;

public class IslandCommand extends BaseCommand {
    public IslandCommand(String name, int permissionLevel) {
        super(name, permissionLevel,true);
    }

    @Override
    protected void onExecute(ServerPlayerEntity player,String arg) {
        ApiResponse<Island> response = HttpUtils.sendRequestV2("GET","v2/island/"+player.getUuidAsString(),"idType=pid");
        try {
            if(response.isSuccess()){
                sendChatMessage(player,"岛id："+response.getData(Island.class).getIslandId());
            }

        } catch (NullPointerException e) {
            sendChatMessage(player,"岛屿不存在!", MessageType.ERROR);
        }
    }
}
