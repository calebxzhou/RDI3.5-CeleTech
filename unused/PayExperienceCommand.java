package calebzhou.rdi.celestech.command.impl;

import calebzhou.rdi.celestech.command.ArgCommand;
import calebzhou.rdi.celestech.command.BaseCommand;
import calebzhou.rdi.celestech.constant.MessageType;
import calebzhou.rdi.celestech.utils.PlayerUtils;
import calebzhou.rdi.celestech.utils.TextUtils;
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
        PlayerUtils.sendChatMessage(toPlayer,player.getScoreboardName()+"给了您"+exp+"级经验.");

    }
}
