package calebzhou.rdi.core.server.module.tickinv;

import calebzhou.rdi.core.server.RdiCoreServer;
import calebzhou.rdi.core.server.utils.ServerUtils;
import net.minecraft.CrashReport;
import net.minecraft.ReportedException;
import net.minecraft.network.protocol.game.ClientboundSetTimePacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.players.PlayerList;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import xyz.nucleoid.fantasy.mixin.MinecraftServerAccess;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

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
				RdiCoreServer.LOGGER.info("ticking {}",key.toString());
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
