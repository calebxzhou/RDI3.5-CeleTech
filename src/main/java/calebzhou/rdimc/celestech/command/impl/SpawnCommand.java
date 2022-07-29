package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.command.BaseCommand;
import calebzhou.rdimc.celestech.constant.WorldConst;
import calebzhou.rdimc.celestech.utils.PlayerUtils;
import calebzhou.rdimc.celestech.utils.ServerUtils;
import net.minecraft.server.level.ServerPlayer;

public class SpawnCommand extends BaseCommand {
    public SpawnCommand(String name, int permissionLevel) {
        super(name, permissionLevel,false);
    }
    @Override
    protected void onExecute(ServerPlayer player,String arg) {
        PlayerUtils.teleport(player, WorldConst.SPAWN_LOCA);
        ServerUtils.executeCommandOnServer("gamemode survival "+player.getScoreboardName());
    }


}