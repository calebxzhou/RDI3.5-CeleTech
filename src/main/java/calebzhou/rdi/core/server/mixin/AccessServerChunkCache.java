package calebzhou.rdi.core.server.mixin;

import net.minecraft.server.level.ServerChunkCache;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Created by calebzhou on 2022-10-01,17:48.
 */
@Mixin(ServerChunkCache.class)
public interface AccessServerChunkCache {
	@Accessor
	ServerChunkCache.MainThreadExecutor getMainThreadProcessor();
}
