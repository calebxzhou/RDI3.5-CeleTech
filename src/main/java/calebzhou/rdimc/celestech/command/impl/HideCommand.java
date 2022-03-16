package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.command.ArgCommand;
import calebzhou.rdimc.celestech.command.BaseCommand;
import calebzhou.rdimc.celestech.utils.PlayerUtils;
import net.minecraft.server.level.ServerPlayer;

public class HideCommand extends BaseCommand {
    public HideCommand(String name, int permissionLevel) {
        super(name, permissionLevel, false);
    }

    @Override
    public void onExecute(ServerPlayer player, String arg) {

    }
}
