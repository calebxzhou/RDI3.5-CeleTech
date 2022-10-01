package net.himeki.mcmtfabric;

import net.himeki.mcmtfabric.commands.ConfigCommand;
import net.himeki.mcmtfabric.commands.StatsCommand;
import net.himeki.mcmtfabric.config.GeneralConfig;
import net.himeki.mcmtfabric.jmx.JMXRegistration;
import net.himeki.mcmtfabric.serdes.SerDesRegistry;
import net.minecraft.world.InteractionResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.command.api.CommandRegistrationCallback;
import org.quiltmc.qsl.lifecycle.api.event.ServerLifecycleEvents;

public class MCMT implements ModInitializer {
    private static final Logger LOGGER = LogManager.getLogger();
    public static GeneralConfig config = new GeneralConfig();

    @Override
    public void onInitialize(ModContainer container) {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.
        LOGGER.info("Initializing MCMTFabric...");

        if (System.getProperty("jmt.mcmt.jmx") != null) {
            JMXRegistration.register();
        }

        StatsCommand.runDataThread();
        SerDesRegistry.init();


        LOGGER.info("MCMT Setting up threadpool...");
        ParallelProcessor.setupThreadPool(GeneralConfig.getParallelism());


        // Listener reg begin
        ServerLifecycleEvents.READY.register(server -> StatsCommand.resetAll());
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> ConfigCommand.register(dispatcher));

    }
}
