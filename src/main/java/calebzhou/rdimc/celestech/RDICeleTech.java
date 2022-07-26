package calebzhou.rdimc.celestech;

import calebzhou.rdimc.celestech.command.CommandRegister;
import calebzhou.rdimc.celestech.constant.FileConst;
import calebzhou.rdimc.celestech.event.impl.PlayerChatEvent;
import calebzhou.rdimc.celestech.model.VirtualStructure;
import calebzhou.rdimc.celestech.model.thread.SpawnMobTimer;
import calebzhou.rdimc.celestech.module.*;
import calebzhou.rdimc.celestech.module.record.*;
import calebzhou.rdimc.celestech.utils.NetworkUtils;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.npc.VillagerTrades;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class RDICeleTech implements ModInitializer {
    //调试模式
    public static final boolean DEBUG = true;
    public static final String MODID ="rdict3";
    public static final Logger LOGGER = LogManager.getLogger(MODID);
    public static final ConcurrentHashMap<String,String> tpaMap = new ConcurrentHashMap<>();
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
    public static void writeFiles() throws IOException{
        LOGGER.info("写入文件中...");
        String s = new Gson().toJson(VirtualStructure.STRUCTURE_MAP);
                LOGGER.info(s);
        FileUtils.write(VirtualStructure.STRUCTURE_FILE,s, StandardCharsets.UTF_8);
    }
    public static void loadFiles() throws IOException{
        if(!FileConst.FOLDER.exists()){
            LOGGER.info("没有配置存储文件夹，正在创建！");
            FileConst.FOLDER.mkdir();
        }
        /*if(!VirtualStructure.STRUCTURE_FILE.exists()){
            LOGGER.info("没有结构存储文件，正在创建！");
            VirtualStructure.STRUCTURE_FILE.createNewFile();
        }
        LOGGER.info("读入结构文件...");
        String struJson = FileUtils.readFileToString(VirtualStructure.STRUCTURE_FILE, StandardCharsets.UTF_8);
        LOGGER.info(struJson);
        if(!StringUtils.isEmpty(struJson)) {
            Type type = new TypeToken<HashMap<String,List<VirtualStructure>>>(){}.getType();
            VirtualStructure.STRUCTURE_MAP= new Gson().fromJson(struJson, type);
            LOGGER.info("结构文件读入成功！");
        }else{
            LOGGER.info("结构文件为空...");
        }*/
    }

    public static MinecraftServer getServer() {
        return server;
    }

    public static Int2ObjectMap<VillagerTrades.ItemListing[]> copyToFastUtilMap(ImmutableMap<Integer, VillagerTrades.ItemListing[]> map) {
        return new Int2ObjectOpenHashMap(map);
    }

}
