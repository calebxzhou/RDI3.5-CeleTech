package calebzhou.rdi.core.server.mixin.mt;

import net.himeki.mcmtfabric.parallelised.fastutil.Int2ObjectConcurrentHashMap;
import net.minecraft.server.level.ChunkMap;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ChunkMap.class)
public abstract class ThreadedAnvilChunkStorageMixin /*extends ChunkStorage implements ChunkHolder.PlayerProvider*/ {

/*    public ThreadedAnvilChunkStorageMixin(Path directory, DataFixer dataFixer, boolean dsync) {
        super(directory, dataFixer, dsync);
    }

    @Shadow
    @Final
    @Mutable
    private Int2ObjectMap<ChunkMap.TrackedEntity> entityTrackers = new Int2ObjectConcurrentHashMap<>();*/
}
