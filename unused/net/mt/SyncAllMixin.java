package calebzhou.rdi.core.server.mixin.mt;

import net.minecraft.world.level.pathfinder.BinaryHeap;
import net.minecraft.world.ticks.LevelChunkTicks;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = {BinaryHeap.class, LevelChunkTicks.class})
public class SyncAllMixin {
}
