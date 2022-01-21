package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.command.NoArgCommand;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.model.ApiResponse;
import calebzhou.rdimc.celestech.model.CoordLocation;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import calebzhou.rdimc.celestech.utils.PlayerUtils;
import net.minecraft.server.network.ServerPlayerEntity;

import static calebzhou.rdimc.celestech.utils.TextUtils.sendChatMessage;

public class LocateCommand extends NoArgCommand {
    public LocateCommand(String name, int permissionLevel) {
        super(name, permissionLevel,true);
    }

    @Override
    protected void onExecute(ServerPlayerEntity player) {
        if(PlayerUtils.getDimensionName(player).equals("minecraft:the_nether") && !player.getEntityName().equals("sampsonnzx")){
            sendChatMessage(player,"您只能在自己的岛屿上设置传送点.", MessageType.ERROR);
            return;
        }
        ApiResponse response = HttpUtils.sendRequest("PUT","island/"+player.getUuidAsString(),"location="+CoordLocation.fromPlayer(player).toString());

        sendChatMessage(player,response);
    }

}
