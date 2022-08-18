package calebzhou.rdimc.celestech.thread;

import calebzhou.rdimc.celestech.utils.HttpUtils;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

//专门为空岛请求准备的线程
public class RdiIslandRequestThread extends Thread{
    public static final RdiIslandRequestThread INSTANCE = new RdiIslandRequestThread();
    private static final Queue<RdiHttpPlayerRequest> requestQueue = new ConcurrentLinkedQueue<>();
    @Override
    public void run() {
        while(true){
            RdiHttpPlayerRequest request = requestQueue.poll();
            if(request!=null){
                HttpUtils.sendRequestAsync(
                        request,
                        request.doOnSuccess,
                        HttpUtils.universalHttpRequestFailureConsumer(request.player)
                );
            }
        }

    }
    public static void addTask(RdiHttpPlayerRequest request){
        requestQueue.add(request);
    }
    private RdiIslandRequestThread(){}
}
