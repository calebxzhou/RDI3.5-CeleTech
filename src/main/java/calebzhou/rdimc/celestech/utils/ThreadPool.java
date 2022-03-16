package calebzhou.rdimc.celestech.utils;

import calebzhou.rdimc.celestech.model.thread.PlayerMotionThread;
import calebzhou.rdimc.celestech.model.thread.PlayerTemperatureThread;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class ThreadPool {
    private final static ExecutorService exe = Executors.newCachedThreadPool();
    public static void newThread(Runnable runnable){
        exe.execute(runnable);
    }


}
