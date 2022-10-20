package calebzhou.rdi.core.server.module.tickinv;

import calebzhou.rdi.core.server.utils.ServerUtils;
import net.minecraft.server.level.ServerLevel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BooleanSupplier;

public class WorldTickThreadManager {

    public static void onServerCallWorldTick(ServerLevel level, BooleanSupplier hasTimeLeft){
        try {
			//ParallelProcessor.callTick(level, hasTimeLeft, RdiCoreServer.getServer());
			level.tick(hasTimeLeft);
        } catch (Exception e) {
            e.printStackTrace();
            ServerUtils.broadcastChatMessage("tick world错误"+e.getMessage()+e.getCause());
        }
    }
	private static final ExecutorService tickPool = Executors.newCachedThreadPool();
	/*public static void onServerCallWorldTick2(
			 BooleanSupplier hasTimeLeft) {
		MinecraftServer server = RdiCoreServer.getServer();
		int tickCount = server.getTickCount();
		PlayerList playerList = server.getPlayerList();
		ProfilerFiller profiler = server.getProfiler();
		((MinecraftServerAccess) server).getLevels().entrySet().parallelStream().forEach(entry->{
			tickPool.submit(()->{
				ResourceKey<Level> key = entry.getKey();
				ServerLevel serverLevel = entry.getValue();
				logger.info("ticking {}",key.toString());
				profiler.push(() -> serverLevel + " " + serverLevel.dimension().location());
				if (tickCount % 20 == 0) {
					profiler.push("timeSync");
					playerList
							.broadcastAll(
									new ClientboundSetTimePacket(serverLevel.getGameTime(), serverLevel.getDayTime(), serverLevel.getGameRules().getBoolean(GameRules.RULE_DAYLIGHT)),
									serverLevel.dimension()
							);
					profiler.pop();
				}

				profiler.push("tick");

				try {
					serverLevel.tick(hasTimeLeft);
				} catch (Throwable var6) {
					CrashReport crashReport = CrashReport.forThrowable(var6, "RDI Exception ticking world");
					serverLevel.fillReportDetails(crashReport);
					throw new ReportedException(crashReport);
				}

				profiler.pop();
				profiler.pop();
			});
		});
		try {
			tickPool.awaitTermination(0, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

	}*/
}
