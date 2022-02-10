package calebzhou.rdimc.celestech.mixin;

import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import net.minecraft.world.poi.PointOfInterestStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ThreadedAnvilChunkStorage.class)
public interface AccessThreadedAnvilChunkStorage {
    @Accessor
    PointOfInterestStorage getPointOfInterestStorage();
}
