package calebzhou.rdimc.celestech;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.server.level.ServerPlayer;

public class RdiMemoryStorage {
    //tpa请求 发送者pid vs 接受者pid
    public static final Object2ObjectOpenHashMap<String,String> tpaMap = new Object2ObjectOpenHashMap<>();
    //玩家名vs省份
    public static final Object2ObjectOpenHashMap<String,String> ipGeoMap = new Object2ObjectOpenHashMap<>();
    //正在挂机的玩家名
    public static final Object2IntOpenHashMap<String> afkMap = new Object2IntOpenHashMap<>();
    //进入服务器的时候，存档没加载的玩家列表vs维度名
    public static final Object2ObjectOpenHashMap<ServerPlayer, String> dimensionNotLoadPlayersMap = new Object2ObjectOpenHashMap<>();
}
