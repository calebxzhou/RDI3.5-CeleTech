package calebzhou.rdi.core.server.misc

import calebzhou.rdi.core.server.model.RdiPlayerProfile
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap

/**
 * Created by calebzhou on 2022-10-16,13:27.
 */
object RdiPlayerProfileManager {
    fun addProfile(uuid: String, rdiPlayerProfile: RdiPlayerProfile) {
        pidProfileMap[uuid]=rdiPlayerProfile
    }

    val pidProfileMap = Object2ObjectOpenHashMap<String, RdiPlayerProfile>()

}
