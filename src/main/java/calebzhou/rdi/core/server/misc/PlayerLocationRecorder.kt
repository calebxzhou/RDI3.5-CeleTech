package calebzhou.rdi.core.server;

import calebzhou.rdi.core.server.model.RdiPlayerLocation;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.server.level.ServerPlayer;

import static calebzhou.rdi.core.server.utils.PlayerUtils.*;

/**
 * Created by calebzhou on 2022-09-26,21:20.
 */
public class RdiPlayerLocationRecorder {
	//玩家pid vs 上次死亡的位置（back指令专用）
	public static final Object2ObjectOpenHashMap<String, RdiPlayerLocation> pidBackPos = new Object2ObjectOpenHashMap<>();

	//记录玩家当前的位置
	public static void record(ServerPlayer player){
		RdiPlayerLocation location = RdiPlayerLocation.create(player);
		sendChatMessage(player,RESPONSE_INFO,"已经记录当前位置%s，使用/back指令可以返回。".formatted(location.toString()));
		pidBackPos.put(player.getStringUUID(), location);
	}
	public static RdiPlayerLocation getLocation(ServerPlayer player){
		return pidBackPos.get(player.getStringUUID());
	}
	public static void remove(ServerPlayer player){
		pidBackPos.remove(player.getStringUUID());
	}
}
