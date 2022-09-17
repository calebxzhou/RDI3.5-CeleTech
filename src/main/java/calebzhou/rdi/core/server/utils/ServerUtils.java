package calebzhou.rdi.core.server.utils;

import calebzhou.rdi.core.server.RdiCoreServer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class ServerUtils {
    public static void broadcastChatMessage(String msg){
        for (ServerPlayer player : RdiCoreServer.getServer().getPlayerList().getPlayers()) {
            PlayerUtils.sendChatMessage(player,msg);
        }
    }
    public static void executeCommandOnServer(String command){
        executeCommandOnSource(command, RdiCoreServer.getServer().createCommandSourceStack());
    }
    public static void executeOnServerThread(Runnable cmd){
        RdiCoreServer.getServer().execute(cmd);
    }
    public static void executeCommandOnSource(String command, CommandSourceStack source){
        MinecraftServer server = RdiCoreServer.getServer();
		Commands commands = server.getCommands();
		commands.performCommand(
				commands.getDispatcher().parse(command, source)
				,command);
    }
	public static void spawnEntity(EntityType entityType, ServerLevel level, BlockPos pos){
		entityType.spawn(level,null,null,null , pos, MobSpawnType.NATURAL,false,false);
	}
    //获取在线玩家 名称
    /*public static List<String> getOnlinePlayerNameList(){
        return RdiCoreServer.getServer().getPlayerList().getPlayers().stream()
                .map(Player::getScoreboardName).toList();
    }
    //获取挂机玩家
    public static List<Map.Entry<String,Integer>> getAfkPlayerList(){
        return PlayerMotionThread.afkPlayersMap.entrySet().stream()
                //在线玩家与挂机玩家map取交集
                .filter(singleAfkEntry -> getOnlinePlayerNameList().contains(singleAfkEntry.getKey()))
                .toList()
        return new ArrayList<>();
    }
    //挂机玩家 做什么?
    public static void getAfkPlayerListDo(Predicate<? super Map.Entry<String,Integer>> predicate,ServerPlayer fromPlayer){
        getAfkPlayerList().stream()
                //玩家说的话里面有没有挂机人的名称
                .filter(predicate)
                .forEach(entry -> {
                    String name = entry.getKey();
                    int seconds = entry.getValue();
                    PlayerUtils.sendChatMessage(fromPlayer, ColorConstants.GRAY+name+" 已经挂机 "+ TimeUtils.secondsToMinute(seconds,"分","秒")+" ,因此对方不一定能够及时回复您.");
                });
    }*/
    //存档
    public static void save(){
        RdiCoreServer.getServer().saveAllChunks(true,true,true);
    }
}
