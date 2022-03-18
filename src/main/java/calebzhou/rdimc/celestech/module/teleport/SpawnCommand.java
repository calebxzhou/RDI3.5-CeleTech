package calebzhou.rdimc.celestech.module.teleport;

import calebzhou.rdimc.celestech.command.BaseCommand;
import calebzhou.rdimc.celestech.constant.WorldConstant;
import calebzhou.rdimc.celestech.utils.*;
import net.minecraft.server.level.ServerPlayer;

import static calebzhou.rdimc.celestech.utils.TextUtils.sendChatMessage;

public class SpawnCommand extends BaseCommand {
    public SpawnCommand(String name, int permissionLevel) {
        super(name, permissionLevel,false);
    }
    @Override
    protected void onExecute(ServerPlayer player,String arg) {
        PlayerUtils.teleport(player, WorldConstant.SPAWN_LOCA);
        ServerUtils.executeCommandOnServer("gamemode survival "+player.getScoreboardName());
    }


}