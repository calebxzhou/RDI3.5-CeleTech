package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.RDICeleTech;
import calebzhou.rdimc.celestech.command.BaseCommand;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.model.PlayerLocation;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

import static calebzhou.rdimc.celestech.utils.PlayerUtils.teleport;
import static calebzhou.rdimc.celestech.utils.TextUtils.sendChatMessage;

public class HomeCommand extends BaseCommand {
    public HomeCommand(String name, int permissionLevel) {
        super(name, permissionLevel,true);
    }

    @Override
    protected void onExecute(ServerPlayer player,String arg) {
        String resp = HttpUtils.sendRequest("get", "island/" + player.getStringUUID());
        if(resp.equals("fail")){
            sendChatMessage(player, MessageType.ERROR,"您没加入任何一岛屿！");
            return;
        }
        PlayerLocation loca = new PlayerLocation(resp);
        loca.world= RDICeleTech.getServer().overworld();
        player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING,20*30,1));
        teleport(player, loca.add(0.5, 2, 0.5));
        sendChatMessage(player,MessageType.SUCCESS,"1");
    }

}
