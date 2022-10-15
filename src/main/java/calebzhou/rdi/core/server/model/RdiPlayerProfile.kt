package calebzhou.rdi.core.server.model

/**
 * Created by calebzhou on 2022-09-18,22:40.
 */
data class RdiPlayerProfile(val uuid: String, val name: String, val pwd: String, val type: String) {
    val isGenuine: Boolean
        get() = "mojang" == type
}
