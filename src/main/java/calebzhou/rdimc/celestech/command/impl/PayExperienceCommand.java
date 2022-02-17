package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.command.BaseCommand;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.utils.PlayerUtils;
import calebzhou.rdimc.celestech.utils.TextUtils;
import net.minecraft.server.network.ServerPlayerEntity;

public class PayExperienceCommand extends BaseCommand {
    public PayExperienceCommand(String name, int permissionLevel) {
        super(name, permissionLevel,false);
    }

    @Override
    protected void onExecute(ServerPlayerEntity player, String arg) {
        String[] split = arg.split(",");
        int exp = Integer.parseInt(split[1]);
        if(!PlayerUtils.checkExpLevel(player,exp)){
            TextUtils.sendChatMessage(player,"您没有这么多经验", MessageType.ERROR);
            return;
        }
        ServerPlayerEntity toPlayer = PlayerUtils.getPlayerByName(split[0]);
        toPlayer.experienceLevel+=exp;
        TextUtils.sendChatMessage(toPlayer,player.getEntityName()+"给了您"+exp+"级经验.");

    }
}
