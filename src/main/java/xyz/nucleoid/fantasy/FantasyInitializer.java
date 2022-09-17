package xyz.nucleoid.fantasy;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import xyz.nucleoid.fantasy.util.VoidChunkGenerator;

public final class FantasyInitializer implements ModInitializer {
    @Override
    public void onInitialize() {
        Registry.register(Registry.CHUNK_GENERATOR, new ResourceLocation(Fantasy.ID, "void"), VoidChunkGenerator.CODEC);
    }
}
