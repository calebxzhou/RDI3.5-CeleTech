package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.command.ArgCommand;
import calebzhou.rdimc.celestech.command.BaseCommand;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.utils.PlayerUtils;
import calebzhou.rdimc.celestech.utils.TextUtils;
import net.minecraft.server.level.ServerPlayer;

public class PayExperienceCommand extends BaseCommand implements ArgCommand {
    public PayExperienceCommand(String name, int permissionLevel) {
        super(name, permissionLevel,false);
    }

    @Override
    public void onExecute(ServerPlayer player, String arg) {
        String[] split = arg.split(",");
        int exp = Integer.parseInt(split[1]);
        PlayerUtils.checkExpLevel(player,exp);
        ServerPlayer toPlayer = PlayerUtils.getPlayerByName(split[0]);
        toPlayer.experienceLevel+=exp;
        TextUtils.sendChatMessage(toPlayer,player.getScoreboardName()+"给了您"+exp+"级经验.");

    }
}
