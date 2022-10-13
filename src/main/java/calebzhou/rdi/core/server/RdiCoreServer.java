package calebzhou.rdi.core.server;

import calebzhou.rdi.core.server.constant.FileConst;
import calebzhou.rdi.core.server.thread.MobSpawningThread;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.Difficulty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.lifecycle.api.event.ServerLifecycleEvents;

import java.io.IOException;
import java.util.SplittableRandom;

public class RdiCoreServer implements ModInitializer {

    public static final Logger LOGGER = LogManager.getLogger(RdiSharedConstants.MOD_ID);
    public static final SplittableRandom RANDOM = new SplittableRandom();
    private static MinecraftServer server;

    @Override
    public void onInitialize(ModContainer modContainer) {
        try {
            loadFiles();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ServerLifecycleEvents.READY.register((server) -> {
            RdiCoreServer.server = server;
            server.getWorldData().setDifficulty(Difficulty.HARD);
			new MobSpawningThread().start();
        });
        RdiNetworkReceiver.INSTANCE.register();
        RdiEvents.INSTANCE.register();

    }
    public static void loadFiles() throws IOException{
        if(!FileConst.getMainFolder().exists()){
            LOGGER.info("没有配置存储文件夹，正在创建！");
            FileConst.getMainFolder().mkdir();
        }
        if(!FileConst.getHwSpecFolder().exists()){
            LOGGER.info("没有hwspec文件夹，正在创建！");
            FileConst.getHwSpecFolder().mkdir();
        }
    }
    public static MinecraftServer getServer() {
        return server;
    }

}
