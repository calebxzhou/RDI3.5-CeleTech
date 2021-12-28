package calebzhou.rdimc.celestech.utils;

import calebzhou.rdimc.celestech.RDICeleTech;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;

public class ServerUtils {
    public static void executeCommandOnServer(String command){
        executeCommandOnSource(command,RDICeleTech.getServer().getCommandSource());
    }
    public static void executeCommandOnSource(String command, ServerCommandSource source){
        MinecraftServer server = RDICeleTech.getServer();
        server.getCommandManager().execute(source,command);
    }
}
