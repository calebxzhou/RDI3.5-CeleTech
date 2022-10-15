package calebzhou.rdi.core.server.constant

import net.minecraft.core.BlockPos

//常量
object RdiSharedConstants {
    //协议版本
    const val PROTOCOL_NAME = "RDI Core 3.7"

    //F3服务器类型
    const val SERVER_TYPE = "rdi-core-c++"
    //调试模式
    const val DEBUG = true
    //微服务地址
    @JvmField
    val SERVICE_ADDR =  "https://${if (DEBUG) "127.0.0.1" else "www.davisoft.cn"}:19198"
    //modid
    const val MOD_ID = "rdict3"

    //空岛存档维度id前缀
    const val ISLAND_DIMENSION_PREFIX = "i_"
    const val ISLAND_DIMENSION_FULL_PREFIX = "$MOD_ID:$ISLAND_DIMENSION_PREFIX"
    //版本号
    const val PROTOCOL_VERSION = 0x37

    //主城
	@JvmField
	val SPAWN_LOCATION = BlockPos(0, 138, 0)
}
