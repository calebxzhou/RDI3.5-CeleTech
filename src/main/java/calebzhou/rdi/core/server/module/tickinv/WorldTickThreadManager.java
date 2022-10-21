package calebzhou.rdi.core.server.module.tickinv;

import calebzhou.rdi.core.server.utils.ServerUtils;
import net.minecraft.server.level.ServerLevel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BooleanSupplier;

public class WorldTickThreadManager {

    public static void onServerCallWorldTick(ServerLevel level, BooleanSupplier hasTimeLeft){
        try {
			level.tick(hasTimeLeft);
        } catch (Exception e) {
            e.printStackTrace();
            ServerUtils.broadcastChatMessage("tick world错误"+e.getMessage()+e.getCause());
        }
    }
}
