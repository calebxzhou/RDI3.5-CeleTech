package calebzhou.rdimc.celestech.mixin;


import net.minecraft.world.level.chunk.storage.ChunkScanAccess;
import net.minecraft.world.level.levelgen.structure.StructureCheck;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(StructureCheck.class)
public interface AccessStructureLocator {
    @Accessor
    ChunkScanAccess getChunkIoWorker();
}
