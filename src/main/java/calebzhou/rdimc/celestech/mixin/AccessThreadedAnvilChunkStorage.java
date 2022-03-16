package calebzhou.rdimc.celestech.mixin;

import net.minecraft.server.level.ChunkMap;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ChunkMap.class)
public interface AccessThreadedAnvilChunkStorage {
    @Accessor
    PoiManager getPointOfInterestStorage();
}
