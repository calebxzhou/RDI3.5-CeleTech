package calebzhou.rdimc.celestech.utils;

import calebzhou.rdimc.celestech.RDICeleTech;
import calebzhou.rdimc.celestech.model.thread.PlayerMotionThread;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ServerUtils {
    public static final List<Integer> httpHistoryDelayList = new ArrayList<>();
    public static void executeCommandOnServer(String command){
        executeCommandOnSource(command,RDICeleTech.getServer().getCommandSource());
    }
    public static void executeCommandOnSource(String command, ServerCommandSource source){
        MinecraftServer server = RDICeleTech.getServer();
        server.getCommandManager().execute(source,command);
    }
    //获取在线玩家
    public static List<String> getOnlinePlayerList(){
        return RDICeleTech.getServer().getPlayerManager().getPlayerList().stream()
                .map(PlayerEntity::getEntityName).toList();
    }
    //获取挂机玩家
    public static List<Map.Entry<String,Integer>> getAfkPlayerList(){
        return PlayerMotionThread.afkPlayersMap.entrySet().stream()
                //在线玩家与挂机玩家map取交集
                .filter(singleAfkEntry -> getOnlinePlayerList().contains(singleAfkEntry.getKey()))
                .toList();
    }
    //存档
    public static void save(){
        RDICeleTech.getServer().save(true,true,true);
    }
    //记录HTTP请求延迟 ms
    public static void recordHttpReqDelay(long t1,long t2){
        int dt= (int) (t2-t1);
        if(httpHistoryDelayList.size()>20) {
            httpHistoryDelayList.remove(0);
        }

        httpHistoryDelayList.add(dt);

    }
}
