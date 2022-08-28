package calebzhou.rdimc.celestech.thread;

import calebzhou.rdimc.celestech.utils.HttpUtils;
import calebzhou.rdimc.celestech.utils.ThreadPool;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

//专门为空岛请求准备的线程
public class RdiIslandRequestThread {
    //private final static ExecutorService exe = Executors.newFixedThreadPool(5) ;
    public static void addTask(RdiHttpPlayerRequest request){
        ThreadPool.newThread(()->HttpUtils.sendRequestAsync(
                request,
                request.doOnSuccess,
                HttpUtils.universalHttpRequestFailureConsumer(request.player)
        ));
    }
}
