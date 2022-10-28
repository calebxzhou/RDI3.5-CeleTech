package calebzhou.rdi.core.server.command.internal

import calebzhou.rdi.core.server.command.RdiInternalCommand
import calebzhou.rdi.core.server.constant.ResponseCode
import calebzhou.rdi.core.server.model.RdiPlayerLocation
import calebzhou.rdi.core.server.utils.PlayerUtils
import calebzhou.rdi.core.server.utils.WorldUtils
import com.mojang.brigadier.context.CommandContext
import net.minecraft.commands.CommandSourceStack

/**
 * Created by calebzhou on 2022-10-17,8:56.
 */
class RiTeleportCommand :RdiInternalCommand("tp") {
    //uuid|dimension|x|y|z|w|p
    override fun execute(context: CommandContext<CommandSourceStack>, arg: List<String>) :Boolean{
        val pid= arg[0]
        val dimension = arg[1]
        val x = arg[2].toDouble()
        val y = arg[3].toDouble()
        val z = arg[4].toDouble()
        val w = arg[5].toDouble()
        val p = arg[6].toDouble()

        val player = PlayerUtils.getPlayerByUuid(pid)?:let{
            feedback(context.source,ResponseCode.TargetOffline)
            return false
        }
        PlayerUtils.teleport(player,
            RdiPlayerLocation.create(WorldUtils.getLevelByDimensionName(dimension)?:let{
                feedback(context.source,ResponseCode.DimensionNotLoaded)
                return false
            },x,y,z,w,p))
        return true
    }

}
