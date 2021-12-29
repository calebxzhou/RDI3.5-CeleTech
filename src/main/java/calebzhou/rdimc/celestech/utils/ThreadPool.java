package calebzhou.rdimc.celestech.utils;

import calebzhou.rdimc.celestech.model.thread.PlayerMonitorThread;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPool {
    private final static ConcurrentHashMap<String,Thread> playerThreadMap = new ConcurrentHashMap<>();
    private final static ExecutorService exe = Executors.newCachedThreadPool();
    public static void newThread(Runnable runnable){
        exe.execute(runnable);
    }
    public static void startPlayerThread(ServerPlayerEntity player){
        Thread thread = new PlayerMonitorThread(player);
        playerThreadMap.put(player.getUuidAsString(),thread);
        thread.start();
    }
    public static void stopPlayerThread(String uuid){
        playerThreadMap.remove(uuid);
    }
    public static boolean isPlayerThreadStarted(String uuid){
        return playerThreadMap.containsKey(uuid);
    }
}
