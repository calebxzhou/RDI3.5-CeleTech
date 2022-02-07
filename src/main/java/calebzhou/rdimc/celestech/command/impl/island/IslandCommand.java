package calebzhou.rdimc.celestech.command.impl.island;

import calebzhou.rdimc.celestech.command.NoArgCommand;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.model.ApiResponse;
import calebzhou.rdimc.celestech.model.CoordLocation;
import calebzhou.rdimc.celestech.model.Island;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import calebzhou.rdimc.celestech.utils.PlayerUtils;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Objects;

import static calebzhou.rdimc.celestech.utils.TextUtils.sendChatMessage;

public class IslandCommand extends NoArgCommand {
    public IslandCommand(String name, int permissionLevel) {
        super(name, permissionLevel,true);
    }

    @Override
    protected void onExecute(ServerPlayerEntity player) {
        ApiResponse<Island> response = HttpUtils.sendRequest("GET","island/"+player.getUuidAsString(),"idType=pid");
        try {
            if(response.isSuccess()){
                sendChatMessage(player,"岛id："+response.getData(Island.class).getIslandId());
            }

        } catch (NullPointerException e) {
            sendChatMessage(player,"空岛不存在!", MessageType.ERROR);
        }
    }
}
