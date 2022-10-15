package calebzhou.rdi.core.server.misc

import calebzhou.rdi.core.server.model.RdiPlayerLocation
import calebzhou.rdi.core.server.model.RdiPlayerLocation.Companion.create
import calebzhou.rdi.core.server.utils.PlayerUtils
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import net.minecraft.server.level.ServerPlayer

/**
 * Created by calebzhou on 2022-09-26,21:20.
 */
object PlayerLocationRecorder {
    //玩家pid vs 位置记录（back指令专用）
    private val pidBackPos = Object2ObjectOpenHashMap<String, RdiPlayerLocation>()

    //记录玩家当前的位置
	@JvmStatic
	fun record(player: ServerPlayer) {
        val location = create(player)
        PlayerUtils.sendChatMessage(
            player,
            PlayerUtils.RESPONSE_INFO,
            "已经记录当前位置${location}，使用/back指令可以返回。"
        )
        pidBackPos[player.stringUUID] = location
    }

    fun getLocation(player: ServerPlayer): RdiPlayerLocation? {
        return pidBackPos[player.stringUUID]
    }

    fun remove(player: ServerPlayer) {
        pidBackPos.remove(player.stringUUID)
    }
}
