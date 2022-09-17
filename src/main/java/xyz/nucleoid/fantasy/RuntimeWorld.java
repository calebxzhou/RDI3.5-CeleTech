package xyz.nucleoid.fantasy;

import com.google.common.collect.ImmutableList;
import net.minecraft.Util;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ProgressListener;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.BiomeManager;
import org.jetbrains.annotations.Nullable;
import xyz.nucleoid.fantasy.mixin.MinecraftServerAccess;
import xyz.nucleoid.fantasy.util.VoidWorldProgressListener;

class RuntimeWorld extends ServerLevel {
    final Style style;

    RuntimeWorld(MinecraftServer server, ResourceKey<Level> registryKey, RuntimeWorldConfig config, Style style) {
        super(
                server, Util.backgroundExecutor(), ((MinecraftServerAccess) server).getStorageSource(),
                new RuntimeWorldProperties(server.getWorldData(), config),
                registryKey,
                config.createDimensionOptions(server),
                VoidWorldProgressListener.INSTANCE,
                false,
                BiomeManager.obfuscateSeed(config.getSeed()),
                ImmutableList.of(),
                config.shouldTickTime()
        );
        this.style = style;
    }

    @Override
    public void save(@Nullable ProgressListener progressListener, boolean flush, boolean enabled) {
        if (this.style == Style.PERSISTENT || !flush) {
            super.save(progressListener, flush, enabled);
        }
    }

    public enum Style {
        PERSISTENT,
        TEMPORARY
    }
}
