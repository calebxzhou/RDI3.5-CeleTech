package calebzhou.rdimc.celestech;

import calebzhou.rdimc.celestech.command.CommandRegister;
import calebzhou.rdimc.celestech.command.impl.TpaCommand;
import calebzhou.rdimc.celestech.event.impl.PlayerBlockEvent;
import calebzhou.rdimc.celestech.event.impl.PlayerConnectEvent;
import calebzhou.rdimc.celestech.event.impl.PlayerMiscEvent;
import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.village.TradeOffers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

public class RDICeleTech implements ModInitializer {
    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LogManager.getLogger("modid");
    public static final HashMap<String, TpaCommand.PlayerTpaRequest> tpaRequestMap = new HashMap<>();

    private static MinecraftServer server;

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.
        ServerLifecycleEvents.SERVER_STARTED.register((server) -> {
            RDICeleTech.server = server;
        });
        new PlayerBlockEvent();
        new CommandRegister();
        new PlayerConnectEvent();
        new PlayerMiscEvent();

    }

    public static MinecraftServer getServer() {
        return server;
    }

    public static Int2ObjectMap<TradeOffers.Factory[]> copyToFastUtilMap(ImmutableMap<Integer, TradeOffers.Factory[]> map) {
        return new Int2ObjectOpenHashMap(map);
    }
}
