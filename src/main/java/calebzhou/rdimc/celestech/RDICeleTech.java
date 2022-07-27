package calebzhou.rdimc.celestech;

import calebzhou.rdimc.celestech.command.CommandRegister;
import calebzhou.rdimc.celestech.constant.FileConst;
import calebzhou.rdimc.celestech.event.impl.PlayerChatEvent;
import calebzhou.rdimc.celestech.model.VirtualStructure;
import calebzhou.rdimc.celestech.model.thread.SpawnMobTimer;
import calebzhou.rdimc.celestech.module.*;
import calebzhou.rdimc.celestech.module.record.*;
import calebzhou.rdimc.celestech.utils.NetworkUtils;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.Difficulty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class RDICeleTech implements ModInitializer {
    //调试模式
    public static final boolean DEBUG = true;
    public static final String MODID ="rdict3";
    public static final Logger LOGGER = LogManager.getLogger(MODID);
    public static final ConcurrentHashMap<String,String> tpaMap = new ConcurrentHashMap<>();
    public static final Object2IntOpenHashMap<String> afkMap = new Object2IntOpenHashMap<>();
    //离线模式玩家列表
    public static final SplittableRandom RANDOM = new SplittableRandom();
    private static MinecraftServer server;
    public static final int VERSION =0x35A;
    @Override
    public void onInitialize() {
        ServerLifecycleEvents.SERVER_STARTED.register((server) -> {
            RDICeleTech.server = server;
            server.getWorldData().setDifficulty(Difficulty.HARD);
            try {
                loadFiles();
            } catch (IOException e) {
                e.printStackTrace();
            }
            new Timer().schedule(new SpawnMobTimer(),0,60*10*1000);
        });
        new CommandRegister();
        new PlayerChatEvent();

        new NetworkUtils();
        loadModules();
    }
    private void loadModules(){
        new AfkDetect().registerNetworking();
        new CheckIslandOnJoin().registerCallbacks();
        new Leap().registerNetworking();
        new SelectArea().registerCallbacks();
        new Weather().registerCallbacks();
        new RecordBlockEvent().registerCallbacks();
        new RecordPlayerAttackEntity().registerCallbacks();
        new RecordPlayerChat().registerCallbacks();
        new RecordPlayerDeath().registerCallbacks();
        new RecordPlayerLogin().registerCallbacks();
        new RecordPlayerLogout().registerCallbacks();
        new RecordPlayerUuidName().registerCallbacks();
    }
    public static void loadFiles() throws IOException{
        if(!FileConst.FOLDER.exists()){
            LOGGER.info("没有配置存储文件夹，正在创建！");
            FileConst.FOLDER.mkdir();
        }
    }

    public static MinecraftServer getServer() {
        return server;
    }

}
