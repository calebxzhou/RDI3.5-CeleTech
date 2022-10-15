package calebzhou.rdi.core.server.constant

import net.minecraft.resources.ResourceLocation

//网络包
object NetworkPackets {

    //C2S 客户端->服务端
    //隔空跳跃
    val LEAP = ResourceLocation(RdiSharedConstants.MOD_ID, "leap")

    //挂机检测
    
	val AFK_DETECT = ResourceLocation(RdiSharedConstants.MOD_ID, "afk_detect")

    //跳舞树

	val DANCE_TREE_GROW = ResourceLocation(RdiSharedConstants.MOD_ID, "dance_tree_grow")

    //硬件信息

	val HW_SPEC = ResourceLocation(RdiSharedConstants.MOD_ID, "hw_spec")

    //右下角消息弹框

	val POPUP = ResourceLocation(RdiSharedConstants.MOD_ID, "popup")

    //对话框信息

	val DIALOG_INFO = ResourceLocation(RdiSharedConstants.MOD_ID, "dialog_info")

	val SAVE_WORLD = ResourceLocation(RdiSharedConstants.MOD_ID, "save_world")

    //快速繁殖

	val ANIMAL_SEX = ResourceLocation(RdiSharedConstants.MOD_ID, "animal_sex")

    //S2C 服务端->客户端
    //玩家天气信息

	val WEATHER = ResourceLocation(RdiSharedConstants.MOD_ID, "weather")

    //玩家地理定位

	val GEO_LOCATION = ResourceLocation(RdiSharedConstants.MOD_ID, "geo_location")

    //岛屿信息
    val ISLAND_INFO = ResourceLocation(RdiSharedConstants.MOD_ID, "island_info")

    //设定密码
    val SET_PASSWORD = ResourceLocation(RdiSharedConstants.MOD_ID, "set_password")
}
