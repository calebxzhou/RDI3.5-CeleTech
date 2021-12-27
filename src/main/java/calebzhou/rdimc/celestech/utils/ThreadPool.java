package calebzhou.rdimc.celestech.utils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPool {
    public final static ConcurrentHashMap<String,Thread> playerThreadMap = new ConcurrentHashMap<>();
    private final static ExecutorService exe = Executors.newFixedThreadPool(65535);
    public static void newThread(Runnable runnable){
        exe.execute(runnable);
    }
    public static void newPlayerThread(String uuid,Thread thread){
        playerThreadMap.put(uuid,thread);
        thread.start();
    }
}
