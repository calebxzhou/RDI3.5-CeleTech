package calebzhou.rdimc.celestech.thread;


import calebzhou.rdimc.celestech.utils.HttpUtils;

import java.util.PriorityQueue;
import java.util.Queue;

//记录线程，发送HTTP请求，专为record类的请求，不处理返回的结果
public class RdiSendRecordThread extends Thread{

    public static final RdiSendRecordThread INSTANCE = new RdiSendRecordThread();
    public static final Queue<RdiHttpRequest> requestQueue = new PriorityQueue<>();

    @Override
    public void run() {
        while(true){
            RdiHttpRequest request = requestQueue.poll();
            if(request!=null){
                HttpUtils.sendRequest(request);
            }
        }

    }

    private RdiSendRecordThread(){}
}
