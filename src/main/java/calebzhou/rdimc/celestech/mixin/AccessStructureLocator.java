package calebzhou.rdimc.celestech.mixin;


import net.minecraft.world.StructureLocator;
import net.minecraft.world.storage.NbtScannable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(StructureLocator.class)
public interface AccessStructureLocator {
    @Accessor
    NbtScannable getChunkIoWorker();
}
