package calebzhou.rdimc.celestech.module.island.command;

import calebzhou.rdimc.celestech.command.BaseCommand;
import calebzhou.rdimc.celestech.model.ApiResponse;
import calebzhou.rdimc.celestech.model.CoordLocation;
import calebzhou.rdimc.celestech.model.Island;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import calebzhou.rdimc.celestech.utils.PlayerUtils;
import java.util.Objects;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

import static calebzhou.rdimc.celestech.utils.TextUtils.sendChatMessage;

public class HomeCommand extends BaseCommand {
    public HomeCommand(String name, int permissionLevel) {
        super(name, permissionLevel,true);
    }

    @Override
    protected void onExecute(ServerPlayer player,String arg) {
        ApiResponse<Island> response = HttpUtils.sendRequestV2("GET","v2/island/"+player.getStringUUID());
            if(response.isSuccess()){
                player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING,20*2,0));
                Island island = response.getData(Island.class);
                PlayerUtils.teleport(player, Objects.requireNonNull(CoordLocation.fromString(Objects.requireNonNull(island).getLocation()).add(0.5,2,0.5)));
            }
            sendChatMessage(player,response);


    }

}
