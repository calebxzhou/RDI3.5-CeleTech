package calebzhou.rdimc.celestech.module.tickinv;

import calebzhou.rdimc.celestech.utils.ServerUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

import java.util.Map;
import java.util.function.BooleanSupplier;

public class WorldTickThreadManager {
    public static WorldTickThreadManager INSTANCE = new WorldTickThreadManager();
    private WorldTickThreadManager(){}
    //维度名 vs 地图tick线程对象 HashMap
   // private Map<String, WorldTickThread> nameLevelMap= new Object2ObjectOpenHashMap<>();
    //当mc本体执行到tickChildren方法时，getAllWorld之前执行这个方法
    public void onServerTickingChildrenWorlds(Map<ResourceKey<Level>, ServerLevel> levels){
        /*levels.forEach((levelResourceKey, serverLevel) -> {
            String location = levelResourceKey.location().toString();
            if(!nameLevelMap.containsKey(location)){
                WorldTickThread tickThread = new WorldTickThread(serverLevel);
                tickThread.start();
                nameLevelMap.put(location, tickThread);
                RDICeleTech.LOGGER.info("正在启动维度tick线程：{}",location);
            }
        });*/
    }
    public void onServerCallWorldTick(ServerLevel level, BooleanSupplier hasTimeLeft){
        try {
             level.tick(hasTimeLeft);
        } catch (Exception e) {
            e.printStackTrace();
            ServerUtils.broadcastChatMessage("tick world错误"+e+e.getCause());
        }
        /*String dimensionName = WorldUtils.getDimensionName(level);
        WorldTickThread worldTickThread = nameLevelMap.get(dimensionName);
        if(hasTimeLeft.getAsBoolean())
            worldTickThread.tickNow();*/
    }
}
