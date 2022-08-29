package calebzhou.rdimc.celestech.thread;

import calebzhou.rdimc.celestech.utils.HttpUtils;
import calebzhou.rdimc.celestech.utils.ThreadPool;

//专门为空岛请求准备的线程
public class RdiRequestThread {
    //private final static ExecutorService exe = Executors.newFixedThreadPool(5) ;
    public static void addTask(RdiHttpPlayerRequest request){
        ThreadPool.newThread(()->HttpUtils.sendRequestAsync(
                request,
                request.doOnSuccess,
                HttpUtils.universalHttpRequestFailureConsumer(request.player)
        ));
    }
}
