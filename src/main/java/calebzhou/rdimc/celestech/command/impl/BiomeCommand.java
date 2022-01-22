package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.command.OneArgCommand;
import net.minecraft.server.network.ServerPlayerEntity;

public class BiomeCommand extends OneArgCommand {
    public BiomeCommand(String name, int permissionLevel) {
        super(name, permissionLevel);
    }

    @Override
    protected void onExecute(ServerPlayerEntity player, String arg) {

    }
}
