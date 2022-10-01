package calebzhou.rdi.core.server.mixin.mt;

import net.himeki.mcmtfabric.parallelised.fastutil.ConcurrentLongLinkedOpenHashSet;
import net.himeki.mcmtfabric.parallelised.fastutil.Long2ObjectConcurrentHashMap;
import net.minecraft.world.level.chunk.storage.SectionStorage;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SectionStorage.class)
public abstract class SerializingRegionBasedStorageMixin<R> implements AutoCloseable {
    /*@Shadow
    @Final
    @Mutable
    private Long2ObjectMap<Optional<R>> loadedElements = new Long2ObjectConcurrentHashMap<>();

    @Shadow
    @Final
    @Mutable
    private LongLinkedOpenHashSet unsavedElements = new ConcurrentLongLinkedOpenHashSet();*/
}
