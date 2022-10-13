package calebzhou.rdi.core.server.mixin;

import net.minecraft.server.level.ChunkMap;
import net.minecraft.world.level.ChunkPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/**
 * Created by calebzhou on 2022-10-06,19:14.
 */
@Mixin(ChunkMap.class)
public interface AccessChunkMap {
	@Invoker
	boolean invokeAnyPlayerCloseEnoughForSpawning(ChunkPos chunkPos);
}
