package calebzhou.rdi.core.server.utils

import calebzhou.rdi.core.server.RdiCoreServer
import net.minecraft.commands.CommandSourceStack
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobSpawnType

object ServerUtils {
    @JvmStatic
    fun broadcastChatMessage(msg: String) {
        broadcastChatMessage(Component.literal(msg))
    }

    fun broadcastChatMessage(msg: Component) {
        for (player in RdiCoreServer.server.playerList.players) {
            PlayerUtils.sendChatMessage(player, msg)
        }
    }

    fun broadcastActionBarMessage(msg: Component) {
        for (player in RdiCoreServer.server.playerList.players) {
            PlayerUtils.sendChatMessage(player, msg, true)
        }
    }

    fun executeCommandOnServer(command: String) {
        executeCommandOnSource(command, RdiCoreServer.server.createCommandSourceStack())
    }

    fun executeOnServerThread(cmd: Runnable) {
        RdiCoreServer.server.execute(cmd)
    }

    fun executeCommandOnSource(command: String, source: CommandSourceStack) {
        val server = RdiCoreServer.server
        val commands = server.commands
        commands.performCommand(
            commands.dispatcher.parse(command, source), command
        )
    }

    fun spawnEntity(entityType: EntityType<*>, level: ServerLevel, pos: BlockPos) {
        entityType.spawn(level, null, null, null, pos, MobSpawnType.NATURAL, false, false)
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
    //挂机玩家 做什么
    public static void getAfkPlayerListDo(Predicate< super Map.Entry<String,Integer>> predicate,ServerPlayer fromPlayer){
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
    fun save() {
        executeOnServerThread{
            RdiCoreServer.server.saveAllChunks(true, true, true)
        }

    }
}
