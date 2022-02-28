package calebzhou.rdimc.celestech.module.teleport;

import calebzhou.rdimc.celestech.command.BaseCommand;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.constant.WorldConstants;
import calebzhou.rdimc.celestech.utils.*;
import net.minecraft.server.network.ServerPlayerEntity;

import static calebzhou.rdimc.celestech.utils.TextUtils.sendChatMessage;

public class SpawnCommand extends BaseCommand {
    public SpawnCommand(String name, int permissionLevel) {
        super(name, permissionLevel,false);
    }
    @Override
    protected void onExecute(ServerPlayerEntity player,String arg) {
        PlayerUtils.teleport(player, WorldConstants.SPAWN_LOCA);
        ServerUtils.executeCommandOnServer("gamemode survival "+player.getEntityName());
    }


}