package calebzhou.rdi.core.server.thread;

import calebzhou.rdi.core.server.utils.HttpUtils;
import calebzhou.rdi.core.server.utils.ThreadPool;

//专门为空岛请求准备的线程
public class RdiRequestThread {
    public static void addTask(RdiHttpPlayerRequest request){
        ThreadPool.newThread(()->HttpUtils.sendRequestAsync(
                request,
                request.doOnSuccess,
                HttpUtils.universalHttpRequestFailureConsumer(request.player)
        ));
    }
}
