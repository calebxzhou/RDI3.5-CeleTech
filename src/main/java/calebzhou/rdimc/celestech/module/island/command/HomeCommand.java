package calebzhou.rdimc.celestech.module.island.command;

import calebzhou.rdimc.celestech.command.BaseCommand;
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

public class HomeCommand extends BaseCommand {
    public HomeCommand(String name, int permissionLevel) {
        super(name, permissionLevel,true);
    }

    @Override
    protected void onExecute(ServerPlayerEntity player,String arg) {
        /*if(!PlayerUtils.isOverworld(player)){
            sendChatMessage(player,"引力太强, 无法离开此地.", MessageType.ERROR);
            return;
        }*/
        ApiResponse<Island> response = HttpUtils.sendRequestV2("GET","v2/island/"+player.getUuidAsString());
            if(response.isSuccess()){
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING,20*2,0));
                PlayerUtils.teleport(player, Objects.requireNonNull(CoordLocation.fromString(Objects.requireNonNull(response.getData(Island.class)).getLocation()).add(0.5,2,0.5)));
            }
            sendChatMessage(player,response);


    }

}
