package calebzhou.rdi.core.server.utils;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPool {
    private final static ExecutorService exe = Executors.newCachedThreadPool();
    public static void newThread(Runnable runnable){
        exe.execute(runnable);
    }

	public static void doAfter(int seconds,Runnable runnable){
		new Timer().schedule(
				new TimerTask() {
					@Override
					public void run() {
						runnable.run();
						this.cancel();
					}
				},
				seconds* 1000L
		);
	}

}
