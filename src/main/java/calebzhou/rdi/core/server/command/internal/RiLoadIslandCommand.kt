package calebzhou.rdi.core.server.command.internal

import calebzhou.rdi.core.server.RdiCoreServer
import calebzhou.rdi.core.server.command.RdiInternalCommand
import calebzhou.rdi.core.server.utils.IslandUtils
import com.mojang.brigadier.context.CommandContext
import net.minecraft.commands.CommandSourceStack
import xyz.nucleoid.fantasy.Fantasy

/**
 * Created by calebzhou on 2022-10-17,8:56.
 */
//载入岛屿维度
class RiLoadIslandCommand:RdiInternalCommand("load-dim") {
    //岛ID | 创建时间(unix时间戳)
    override fun execute(context: CommandContext<CommandSourceStack>, arg: List<String>) :Boolean{
        val islandDimension =  IslandUtils.getIslandDimensionLoca(arg[0])
        val createTime = arg[2].toLong()
        val serverLevel = Fantasy.get(RdiCoreServer.server)
            .getOrOpenPersistentWorld(islandDimension, IslandUtils.getIslandWorldConfig(createTime))
            .asWorld()
        return true
    }

}
