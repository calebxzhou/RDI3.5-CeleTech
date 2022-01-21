package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.command.NoArgCommand;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.model.ApiResponse;
import calebzhou.rdimc.celestech.model.CoordLocation;
import calebzhou.rdimc.celestech.model.Island;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import calebzhou.rdimc.celestech.utils.PlayerUtils;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Objects;

import static calebzhou.rdimc.celestech.utils.TextUtils.sendChatMessage;

public class HomeCommand extends NoArgCommand {
    public HomeCommand(String name, int permissionLevel) {
        super(name, permissionLevel,true);
    }

    @Override
    protected void onExecute(ServerPlayerEntity player) {
        if(PlayerUtils.getDimensionName(player).equals("minecraft:the_nether") && !player.getEntityName().equals("sampsonnzx")){
            sendChatMessage(player,"引力太强, 无法离开此地.", MessageType.ERROR);
            return;
        }
        ApiResponse<Island> response = HttpUtils.sendRequest("GET","island/"+player.getUuidAsString(),"idType=pid");
        try {
            PlayerUtils.teleport(player, Objects.requireNonNull(CoordLocation.fromString(Objects.requireNonNull(response.getData(Island.class)).getLocation()).add(0.5,2,0.5)));
            sendChatMessage(player,response);
        } catch (NullPointerException e) {
            sendChatMessage(player,"空岛不存在!",MessageType.ERROR);
        }

    }

}
