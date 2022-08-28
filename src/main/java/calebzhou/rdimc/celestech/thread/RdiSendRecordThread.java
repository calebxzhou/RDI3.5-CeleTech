package calebzhou.rdimc.celestech.thread;


import calebzhou.rdimc.celestech.utils.HttpUtils;
import calebzhou.rdimc.celestech.utils.ThreadPool;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

//记录线程，发送HTTP请求，专为record类的请求，不处理返回的结果
public class RdiSendRecordThread {
    /*private final static ThreadFactory namedThreadFactory =
            new ThreadFactoryBuilder().setNameFormat("RdiRecordRequest-%d").build();
    private final static ExecutorService exe = Executors.newFixedThreadPool(3,namedThreadFactory) ;*/
    public static void addTask(RdiHttpRequest request){
        ThreadPool.newThread(()->HttpUtils.sendRequestAsync(request,msg->{},HttpUtils::universalHttpRequestFailureConsumer));
    }
}
