package calebzhou.rdi.core.server.model

import calebzhou.rdi.core.server.utils.WorldUtils
import net.minecraft.core.Vec3i
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.phys.Vec3
import java.io.Serializable

/**
 * Created by calebzhou on 2022-09-23,20:54.
 */
class RdiPlayerLocation private constructor(
    var level: ServerLevel,
    var x: Double,
    var y: Double,
    var z: Double,
    var w: Double,
    var p: Double
) : Serializable {

    override fun toString(): String {
        return "{" + WorldUtils.getDimensionName(level) +
                ", x=" + Math.round(x) +
                ", y=" + Math.round(y) +
                ", z=" + Math.round(z) +
                ", w=" + Math.round(w) +
                ", p=" + Math.round(p) +
                '}'
    }

    companion object {
        fun create(level: ServerLevel, x: Double, y: Double, z: Double, w: Double, p: Double): RdiPlayerLocation {
            return RdiPlayerLocation(level, x, y, z, w, p)
        }

        fun create(level: ServerLevel, vec3i: Vec3i): RdiPlayerLocation {
            return RdiPlayerLocation(level, vec3i.x.toDouble(), vec3i.y.toDouble(), vec3i.z.toDouble(), 0.0, 0.0)
        }

        fun create(level: ServerLevel, vec3: Vec3): RdiPlayerLocation {
            return RdiPlayerLocation(level, vec3.x(), vec3.y(), vec3.z(), 0.0, 0.0)
        }

        @JvmStatic
		fun create(player: ServerPlayer): RdiPlayerLocation {
            return RdiPlayerLocation(
                player.getLevel(),
                player.x,
                player.y,
                player.z,
                player.yRot.toDouble(),
                player.xRot.toDouble()
            )
        }
    }
}
