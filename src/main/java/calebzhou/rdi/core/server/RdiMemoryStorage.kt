package calebzhou.rdi.core.server

import calebzhou.rdi.core.server.command.RdiCommand
import calebzhou.rdi.core.server.model.RdiGeoLocation
import calebzhou.rdi.core.server.model.RdiUser
import calebzhou.rdi.core.server.model.RdiWeather
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet

object RdiMemoryStorage {
    //tpa请求 接受者pid 发送者pid
	@JvmField
	val tpaMap = Object2ObjectOpenHashMap<String, String>()

    //pid vs 玩家地理位置
	@JvmField
	val pidGeoMap = Object2ObjectOpenHashMap<String, RdiGeoLocation>()

    //pid vs 玩家天气
	@JvmField
	val pidWeatherMap = Object2ObjectOpenHashMap<String, RdiWeather>()
    @JvmField
	val pidUserMap = Object2ObjectOpenHashMap<String, RdiUser>()

    //正在挂机的玩家名
	@JvmField
	val afkMap = Object2IntOpenHashMap<String>()

    //准备传送回主城的玩家pid列表
	@JvmField
	val pidBeingGoSpawn = ObjectArrayList<String>()
    @JvmField
	val pidToSpeakPlayersMap = Object2ObjectOpenHashMap<String, ObjectOpenHashSet<String>>()
    @JvmField
	val commandSet: Set<RdiCommand> = ObjectOpenHashSet() //准备卸载的维度队列
    //public static final Queue<ServerLevel> levelsBeingUnloadQueue = new LinkedList<>();
}
