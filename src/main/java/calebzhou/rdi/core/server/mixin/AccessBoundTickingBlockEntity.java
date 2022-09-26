package calebzhou.rdi.core.server.mixin;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Created by calebzhou on 2022-09-26,7:07.
 */

@Mixin(LevelChunk.BoundTickingBlockEntity.class)
public interface AccessBoundTickingBlockEntity <T extends BlockEntity>{
	@Accessor
	T getBlockEntity();
	@Accessor
	BlockEntityTicker<T> getTicker();
	@Accessor
	boolean getLoggedInvalidBlockState();
}
