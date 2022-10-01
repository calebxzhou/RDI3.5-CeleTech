package calebzhou.rdi.core.server;

import calebzhou.rdi.core.server.command.RdiCommand;
import calebzhou.rdi.core.server.model.*;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

import java.util.Set;

public class RdiMemoryStorage {
    //tpa请求 接受者pid 发送者pid
    public static final Object2ObjectOpenHashMap<String,String> tpaMap = new Object2ObjectOpenHashMap<>();
	//pid vs 玩家地理位置
	public static final Object2ObjectOpenHashMap<String,RdiGeoLocation> pidGeoMap = new Object2ObjectOpenHashMap<>();
	//pid vs 玩家天气
	public static final Object2ObjectOpenHashMap<String,RdiWeather> pidWeatherMap = new Object2ObjectOpenHashMap<>();
	public static final Object2ObjectOpenHashMap<String, RdiUser> pidUserMap = new Object2ObjectOpenHashMap<>();
    //正在挂机的玩家名
    public static final Object2IntOpenHashMap<String> afkMap = new Object2IntOpenHashMap<>();
	//准备传送回主城的玩家pid列表
	public static final ObjectArrayList<String> pidBeingGoSpawn = new ObjectArrayList<>();
	public static final Object2ObjectOpenHashMap<String,ObjectOpenHashSet<String>> pidToSpeakPlayersMap = new Object2ObjectOpenHashMap<>();
	public static final Set<RdiCommand> commandSet = new ObjectOpenHashSet<>();
	//准备卸载的维度队列
	//public static final Queue<ServerLevel> levelsBeingUnloadQueue = new LinkedList<>();
}
