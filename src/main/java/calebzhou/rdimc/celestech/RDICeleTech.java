package calebzhou.rdimc.celestech;

import calebzhou.rdimc.celestech.command.CommandRegister;
import calebzhou.rdimc.celestech.command.impl.TpaCommand;
import calebzhou.rdimc.celestech.event.EntityBlockEvents;
import calebzhou.rdimc.celestech.event.PlayerConnectionEvents;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
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
		LOGGER.info("Hello Fabric world!");
		ServerLifecycleEvents.SERVER_STARTED.register((server)->{
			RDICeleTech.server = server;
		});
		new EntityBlockEvents();
		new CommandRegister();
		new PlayerConnectionEvents();
	}

	public static MinecraftServer getServer(){
		return server;
	}
}
