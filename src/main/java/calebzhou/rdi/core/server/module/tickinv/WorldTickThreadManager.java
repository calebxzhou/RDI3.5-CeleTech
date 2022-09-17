package calebzhou.rdi.core.server.module.tickinv;

import calebzhou.rdi.core.server.utils.ServerUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

import java.util.Map;
import java.util.function.BooleanSupplier;

public class WorldTickThreadManager {
    public static WorldTickThreadManager INSTANCE = new WorldTickThreadManager();
    private WorldTickThreadManager(){}

    public void onServerCallWorldTick(ServerLevel level, BooleanSupplier hasTimeLeft){
        try {
             level.tick(hasTimeLeft);
        } catch (Exception e) {
            e.printStackTrace();
            ServerUtils.broadcastChatMessage("tick world错误"+e+e.getCause());
        }
    }
}
