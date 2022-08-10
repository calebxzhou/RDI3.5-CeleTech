package calebzhou.rdimc.celestech.utils;

import calebzhou.rdimc.celestech.RDICeleTech;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class ServerUtils {
    public static void broadcastChatMessage(String msg){
        for (ServerPlayer player : RDICeleTech.getServer().getPlayerList().getPlayers()) {
            TextUtils.sendChatMessage(player,msg);
        }
    }
    public static final List<Integer> httpHistoryDelayList = new ArrayList<>();
    public static double getMillisecondPerTick(){
        return MathUtils.getAverageValue(RDICeleTech.getServer().tickTimes) * 1.0E-6D;
    }
    public static void executeCommandOnServer(String command){
        executeCommandOnSource(command,RDICeleTech.getServer().createCommandSourceStack());
    }
    public static void executeCommandOnSource(String command, CommandSourceStack source){
        MinecraftServer server = RDICeleTech.getServer();
        server.getCommands().performCommand(source,command);
    }
    //获取在线玩家 名称
    public static List<String> getOnlinePlayerNameList(){
        return RDICeleTech.getServer().getPlayerList().getPlayers().stream()
                .map(Player::getScoreboardName).toList();
    }
    //获取挂机玩家
    public static List<Map.Entry<String,Integer>> getAfkPlayerList(){
        /*return PlayerMotionThread.afkPlayersMap.entrySet().stream()
                //在线玩家与挂机玩家map取交集
                .filter(singleAfkEntry -> getOnlinePlayerNameList().contains(singleAfkEntry.getKey()))
                .toList()*/
        return new ArrayList<>();
    }
    //挂机玩家 做什么?
    public static void getAfkPlayerListDo(Predicate<? super Map.Entry<String,Integer>> predicate,ServerPlayer fromPlayer){
        /*getAfkPlayerList().stream()
                //玩家说的话里面有没有挂机人的名称
                .filter(predicate)
                .forEach(entry -> {
                    String name = entry.getKey();
                    int seconds = entry.getValue();
                    TextUtils.sendChatMessage(fromPlayer, ColorConstants.GRAY+name+" 已经挂机 "+ TimeUtils.secondsToMinute(seconds,"分","秒")+" ,因此对方不一定能够及时回复您.");
                });*/
    }
    //存档
    public static void save(){
        RDICeleTech.getServer().saveAllChunks(true,true,true);
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
