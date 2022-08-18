package calebzhou.rdimc.celestech;

import calebzhou.rdimc.celestech.constant.FileConst;
import calebzhou.rdimc.celestech.module.TickInverter;
import calebzhou.rdimc.celestech.thread.RdiSendRecordThread;
import calebzhou.rdimc.celestech.utils.ThreadPool;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.Difficulty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.SplittableRandom;
import java.util.concurrent.ConcurrentHashMap;

public class RDICeleTech implements ModInitializer {
    //调试模式
    public static final boolean DEBUG = true;
    public static final String MODID ="rdict3";
    public static final String ISLAND_DIMENSION_PREFIX ="island_";
    public static final Logger LOGGER = LogManager.getLogger(MODID);
    public static final ConcurrentHashMap<String,String> tpaMap = new ConcurrentHashMap<>();
    public static final Object2ObjectOpenHashMap<String,String> ipGeoMap = new Object2ObjectOpenHashMap();
    public static final Object2IntOpenHashMap<String> afkMap = new Object2IntOpenHashMap<>();
    //离线模式玩家列表
    public static final SplittableRandom RANDOM = new SplittableRandom();
    private static MinecraftServer server;
    public static final int VERSION =0x35A;
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
        new NetworkReceiver();
        new FabricEventRegister();
        RdiSendRecordThread.INSTANCE.start();
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
