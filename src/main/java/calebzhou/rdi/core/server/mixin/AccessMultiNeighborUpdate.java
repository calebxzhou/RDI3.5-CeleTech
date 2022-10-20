package calebzhou.rdi.core.server.mixin;


import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.redstone.CollectingNeighborUpdater;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Created by calebzhou on 2022-09-26,9:18.
 */
@Mixin(CollectingNeighborUpdater.MultiNeighborUpdate.class)
public interface AccessMultiNeighborUpdate {
	@Accessor
	BlockPos getSourcePos();
	@Accessor
	Block getSourceBlock();
	@Accessor
	Direction getSkipDirection();
	@Accessor
	int getIdx();
}
