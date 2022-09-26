package calebzhou.rdi.core.server.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.redstone.CollectingNeighborUpdater;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/**
 * Created by calebzhou on 2022-09-26,9:16.
 */
@Mixin(CollectingNeighborUpdater.class)
public interface AccessCollectingNeighborUpdater {
	@Invoker
	void invokeAddAndRun(BlockPos pos, CollectingNeighborUpdater.NeighborUpdates updates);

}
