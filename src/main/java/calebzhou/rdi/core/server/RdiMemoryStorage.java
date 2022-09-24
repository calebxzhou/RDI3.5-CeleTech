package calebzhou.rdi.core.server;

import calebzhou.rdi.core.server.command.RdiCommand;
import calebzhou.rdi.core.server.model.*;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.function.Consumer;

public class RdiMemoryStorage {
    //tpa请求 接受者pid 发送者pid
    public static final Object2ObjectOpenHashMap<String,String> tpaMap = new Object2ObjectOpenHashMap<>();
	//pid vs 玩家地理位置
	public static final Object2ObjectOpenHashMap<String,RdiGeoLocation> pidGeoMap = new Object2ObjectOpenHashMap<>();
	public static final Object2ObjectOpenHashMap<String, RdiUser> pidUserMap = new Object2ObjectOpenHashMap<>();
    //正在挂机的玩家名
    public static final Object2IntOpenHashMap<String> afkMap = new Object2IntOpenHashMap<>();
	//玩家pid vs 执行传送前的位置（back指令专用）
	public static final Object2ObjectOpenHashMap<String, RdiPlayerLocation> pidBackPos = new Object2ObjectOpenHashMap<>();
	//准备传送回主城的玩家pid列表
	public static final ObjectArrayList<String> pidBeingGoSpawn = new ObjectArrayList<>();
	public static final Set<RdiCommand> commandSet = new ObjectOpenHashSet<>();
	//准备卸载的维度队列
	//public static final Queue<ServerLevel> levelsBeingUnloadQueue = new LinkedList<>();
}
