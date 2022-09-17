package xyz.nucleoid.fantasy;

import com.mojang.serialization.Lifecycle;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.apache.commons.io.FileUtils;
import org.quiltmc.qsl.lifecycle.api.event.ServerWorldLoadEvents;
import xyz.nucleoid.fantasy.mixin.MinecraftServerAccess;

import java.io.File;
import java.io.IOException;

final class RuntimeWorldManager {
    private final MinecraftServer server;
    private final MinecraftServerAccess serverAccess;

    RuntimeWorldManager(MinecraftServer server) {
        this.server = server;
        this.serverAccess = (MinecraftServerAccess) server;
    }

    RuntimeWorld add(ResourceKey<Level> worldKey, RuntimeWorldConfig config, RuntimeWorld.Style style) {
        LevelStem options = config.createDimensionOptions(this.server);

        if (style == RuntimeWorld.Style.TEMPORARY) {
            ((FantasyDimensionOptions) (Object) options).fantasy$setSave(false);
        }

        MappedRegistry<LevelStem> dimensionsRegistry = getDimensionsRegistry(this.server);
        boolean isFrozen = ((RemoveFromRegistry<?>) dimensionsRegistry).fantasy$isFrozen();
        ((RemoveFromRegistry<?>) dimensionsRegistry).fantasy$setFrozen(false);
        dimensionsRegistry.register(ResourceKey.create(Registry.LEVEL_STEM_REGISTRY, worldKey.location()), options, Lifecycle.stable());
        ((RemoveFromRegistry<?>) dimensionsRegistry).fantasy$setFrozen(isFrozen);

        RuntimeWorld world = new RuntimeWorld(this.server, worldKey, config, style);

        this.serverAccess.getLevels().put(world.dimension(), world);
		ServerWorldLoadEvents.LOAD.invoker().loadWorld(this.server, world);


        // tick the world to ensure it is ready for use right away
        world.tick(() -> true);

        return world;
    }
	boolean unload(ServerLevel world){
		ResourceKey<Level> dimensionKey = world.dimension();
		Fantasy.LOGGER.info("unloading world {}", dimensionKey.toString());
		if (this.serverAccess.getLevels().remove(dimensionKey, world)) {
			ServerWorldLoadEvents.UNLOAD.invoker().unloadWorld(this.server, world);
			MappedRegistry<LevelStem> dimensionsRegistry = getDimensionsRegistry(this.server);
			RemoveFromRegistry.remove(dimensionsRegistry, dimensionKey.location());
			Fantasy.LOGGER.info("unloading world {} successful", dimensionKey.toString());
			return true;
		}
		return false;
	}
    void delete(ServerLevel world) {
		if(!unload(world))
			return;
		ResourceKey<Level> dimensionKey = world.dimension();
		LevelStorageSource.LevelStorageAccess session = this.serverAccess.getStorageSource();
		File worldDirectory = session.getDimensionPath(dimensionKey).toFile();
		if (worldDirectory.exists()) {
                try {
                    FileUtils.deleteDirectory(worldDirectory);
                } catch (IOException e) {
                    Fantasy.LOGGER.warn("Failed to delete world directory", e);
                    try {
                        FileUtils.forceDeleteOnExit(worldDirectory);
                    } catch (IOException ignored) {
                    }
                }
            }
    }

    private static MappedRegistry<LevelStem> getDimensionsRegistry(MinecraftServer server) {
        WorldGenSettings generatorOptions = server.getWorldData().worldGenSettings();
        return (MappedRegistry<LevelStem>) generatorOptions.dimensions();
    }
}
