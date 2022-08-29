package calebzhou.rdimc.celestech;

import calebzhou.rdimc.celestech.constant.FileConst;
import calebzhou.rdimc.celestech.module.tickinv.TickInverter;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.Difficulty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.SplittableRandom;

public class RDICeleTech implements ModInitializer {

    public static final Logger LOGGER = LogManager.getLogger(RdiSharedConstants.MOD_ID);
    public static final SplittableRandom RANDOM = new SplittableRandom();
    private static MinecraftServer server;

    @Override
    public void onInitialize() {
        try {
            loadFiles();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ServerLifecycleEvents.SERVER_STARTED.register((server) -> {
            RDICeleTech.server = server;
            server.getWorldData().setDifficulty(Difficulty.HARD);
        });
        NetworkReceiver.INSTANCE.register();
        RdiEvents.INSTANCE.register();
        TickInverter.INSTANCE.init();
    }
    public static void loadFiles() throws IOException{
        if(!FileConst.getMainFolder().exists()){
            LOGGER.info("没有配置存储文件夹，正在创建！");
            FileConst.getMainFolder().mkdir();
        }
        if(!FileConst.getPasswordFolder().exists()){
            LOGGER.info("没有密码存储文件夹，正在创建！");
            FileConst.getPasswordFolder().mkdir();
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
