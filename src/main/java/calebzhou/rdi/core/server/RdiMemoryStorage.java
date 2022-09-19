package calebzhou.rdi.core.server;

import calebzhou.rdi.core.server.command.RdiCommand;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.server.level.ServerPlayer;

import java.util.Set;
import java.util.function.Consumer;

public class RdiMemoryStorage {
    //tpa请求 接受者pid 发送者pid
    public static final Object2ObjectOpenHashMap<String,String> tpaMap = new Object2ObjectOpenHashMap<>();
    //玩家名vs省份
    public static final Object2ObjectOpenHashMap<String,String> ipGeoMap = new Object2ObjectOpenHashMap<>();
    //正在挂机的玩家名
    public static final Object2IntOpenHashMap<String> afkMap = new Object2IntOpenHashMap<>();
	public static final Set<RdiCommand> commandSet = new ObjectOpenHashSet<>();
}
