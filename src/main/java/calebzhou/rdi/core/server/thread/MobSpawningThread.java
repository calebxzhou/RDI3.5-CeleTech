package calebzhou.rdi.core.server.thread;

import calebzhou.rdi.core.server.RdiCoreServer;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

/**
 * Created by calebzhou on 2022-10-06,9:41.
 */
public class MobSpawningThread extends Thread{
	private static final Queue<Runnable> mobSpawnQueue = new LinkedList<>();
	@Override
	public void run() {
		while (RdiCoreServer.getServer().isRunning()){
			try {
				if(mobSpawnQueue.size()>0)
					mobSpawnQueue.poll().run();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	public static void addTask(Runnable runnable){
		mobSpawnQueue.add(runnable);
	}
}
