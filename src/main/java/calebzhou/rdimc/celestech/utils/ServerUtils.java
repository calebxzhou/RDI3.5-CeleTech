package calebzhou.rdimc.celestech.utils;

import calebzhou.rdimc.celestech.RDICeleTech;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ServerUtils {
    public static void executeCommandOnServer(String command){
        executeCommandOnSource(command,RDICeleTech.getServer().getCommandSource());
    }
    public static void executeCommandOnSource(String command, ServerCommandSource source){
        MinecraftServer server = RDICeleTech.getServer();
        server.getCommandManager().execute(source,command);
    }
    public static List<String> getOnlinePlayerList(){
        ArrayList<String> list=new ArrayList();
        List<ServerPlayerEntity> playerList = RDICeleTech.getServer().getPlayerManager().getPlayerList();
        Iterator<ServerPlayerEntity> iterator = playerList.stream().iterator();
        while(iterator.hasNext()){
            String name = iterator.next().getDisplayName().getString();
            list. add(name);
        }
        return list;
    }
}
