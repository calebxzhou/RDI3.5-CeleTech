package calebzhou.rdimc.celestech.command;

import net.minecraft.server.network.ServerPlayerEntity;

public interface ArgCommand {
    void onExecute(ServerPlayerEntity player, String arg);
}
