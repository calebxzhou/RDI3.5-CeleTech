package calebzhou.rdi.core.server

import calebzhou.rdi.core.server.command.RdiCommand
import calebzhou.rdi.core.server.model.RdiGeoLocation
import calebzhou.rdi.core.server.model.RdiPlayerProfile
import calebzhou.rdi.core.server.model.RdiWeather
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet

object RdiMemoryStorage {
    //tpa请求 接受者pid 发送者pid

	val tpaMap = Object2ObjectOpenHashMap<String, String>()





    //正在挂机的玩家名

	val afkMap = Object2IntOpenHashMap<String>()

    //准备传送回主城的玩家pid列表

	val pidBeingGoSpawn = ObjectArrayList<String>()

	val pidToSpeakPlayersMap = Object2ObjectOpenHashMap<String, ObjectOpenHashSet<String>>()

	val commandSet: Set<RdiCommand> = ObjectOpenHashSet() //准备卸载的维度队列
    //public static final Queue<ServerLevel> levelsBeingUnloadQueue = new LinkedList<>();
}
