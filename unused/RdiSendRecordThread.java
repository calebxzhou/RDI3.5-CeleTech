package calebzhou.rdi.core.server.thread;


import calebzhou.rdi.core.server.utils.HttpUtils;
import calebzhou.rdi.core.server.utils.ThreadPool;

//记录线程，发送HTTP请求，专为record类的请求，不处理返回的结果
public class RdiSendRecordThread {
    /*private final static ThreadFactory namedThreadFactory =
            new ThreadFactoryBuilder().setNameFormat("RdiRecordRequest-%d").build();
    private final static ExecutorService exe = Executors.newFixedThreadPool(3,namedThreadFactory) ;*/
    public static void addTask(RdiHttpRequest request){
        ThreadPool.newThread(()->HttpUtils.sendRequestAsync(request,msg->{},HttpUtils::universalHttpRequestFailureConsumer));
    }
}
