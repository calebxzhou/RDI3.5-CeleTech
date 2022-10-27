package calebzhou.rdi.core.server

import calebzhou.rdi.core.server.constant.FileConst
import calebzhou.rdi.core.server.constant.RdiSharedConstants
import calebzhou.rdi.core.server.misc.NetPackReceiver.Companion.register
import calebzhou.rdi.core.server.misc.RdiMobSpawner
import calebzhou.rdi.core.server.module.tickinv.EntityTicking
import net.minecraft.server.MinecraftServer
import net.minecraft.world.Difficulty
import org.apache.logging.log4j.LogManager
import org.quiltmc.loader.api.ModContainer
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer
import org.quiltmc.qsl.lifecycle.api.event.ServerLifecycleEvents
import java.io.IOException
import java.util.*

val logger = LogManager.getLogger(RdiSharedConstants.MOD_ID)
class RdiCoreServer : ModInitializer {
    override fun onInitialize(modContainer: ModContainer) {

        try {
            loadFiles()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        ServerLifecycleEvents.READY.register(ServerLifecycleEvents.Ready { server: MinecraftServer ->
            Companion.server = server
            server.worldData.difficulty = Difficulty.HARD
        })
        register()
        RdiEvents().register()
        calebzhou.rdi.core.server.misc.RestartScheduler
    }

    companion object {
        @JvmField
		val RANDOM = SplittableRandom()
        @JvmStatic
		lateinit var server: MinecraftServer
            private set

        @Throws(IOException::class)
        fun loadFiles() {
            if (!FileConst.getMainFolder().exists()) {
                logger.info("没有配置存储文件夹，正在创建！")
                FileConst.getMainFolder().mkdir()
            }
            if (!FileConst.getHwSpecFolder().exists()) {
                logger.info("没有hwspec文件夹，正在创建！")
                FileConst.getHwSpecFolder().mkdir()
            }
        }
    }
}
