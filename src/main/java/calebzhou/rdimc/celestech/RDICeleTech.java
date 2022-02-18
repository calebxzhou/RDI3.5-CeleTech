package calebzhou.rdimc.celestech;

import calebzhou.rdimc.celestech.command.CommandRegister;
import calebzhou.rdimc.celestech.constant.FileConst;
import calebzhou.rdimc.celestech.event.impl.PlayerBlockEvent;
import calebzhou.rdimc.celestech.event.impl.PlayerChatEvent;
import calebzhou.rdimc.celestech.event.impl.PlayerConnectEvent;
import calebzhou.rdimc.celestech.event.impl.PlayerMiscEvent;
import calebzhou.rdimc.celestech.model.VirtualStructure;
import calebzhou.rdimc.celestech.model.thread.SpawnMobTimer;
import com.google.common.collect.ImmutableMap;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.village.TradeOffers;
import net.minecraft.world.Difficulty;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class RDICeleTech implements ModInitializer {
    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LogManager.getLogger("rdict3");
    public static final ConcurrentHashMap<String,String> tpaMap = new ConcurrentHashMap<>();
    public static final SplittableRandom RANDOM = new SplittableRandom();
    private static MinecraftServer server;
    public static final int VERSION =0x3B5;
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.
        ServerLifecycleEvents.SERVER_STARTED.register((server) -> {
            RDICeleTech.server = server;
            server.getSaveProperties().setDifficulty(Difficulty.HARD);
            try {
                loadFiles();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //new Timer().schedule(new SpawnMobTimer(),0,60*3*1000);
        });
        new PlayerBlockEvent();
        new CommandRegister();
        new PlayerConnectEvent();
        new PlayerMiscEvent();
        new PlayerChatEvent();



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
        if(!VirtualStructure.STRUCTURE_FILE.exists()){
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
        }
    }

    public static MinecraftServer getServer() {
        return server;
    }

    public static Int2ObjectMap<TradeOffers.Factory[]> copyToFastUtilMap(ImmutableMap<Integer, TradeOffers.Factory[]> map) {
        return new Int2ObjectOpenHashMap(map);
    }

}
