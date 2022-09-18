package calebzhou.rdi.core.server;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.Consumer;

public class RdiMemoryStorage {
    //tpa请求 接受者pid 发送者pid
    public static final Object2ObjectOpenHashMap<String,String> tpaMap = new Object2ObjectOpenHashMap<>();
    //玩家名vs省份
    public static final Object2ObjectOpenHashMap<String,String> ipGeoMap = new Object2ObjectOpenHashMap<>();
    //正在挂机的玩家名
    public static final Object2IntOpenHashMap<String> afkMap = new Object2IntOpenHashMap<>();

}
