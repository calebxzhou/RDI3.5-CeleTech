package calebzhou.rdimc.celestech.command;

import net.minecraft.server.level.ServerPlayer;

public interface ArgCommand {
    void onExecute(ServerPlayer player, String arg);
}
