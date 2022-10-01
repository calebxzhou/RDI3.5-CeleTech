package calebzhou.rdi.core.server.mixin.mt;

import net.himeki.mcmtfabric.parallelised.fastutil.ConcurrentLongSortedSet;
import net.minecraft.world.level.entity.EntityAccess;
import net.minecraft.world.level.entity.EntitySectionStorage;
import org.spongepowered.asm.mixin.Mixin;


@Mixin(EntitySectionStorage.class)
public abstract class SectionedEntityCacheMixin<T extends EntityAccess> {

//    @Shadow
//    @Final
//    private final Long2ObjectMap<EntityTrackingSection<T>> trackingSections = new Long2ObjectConcurrentHashMap<>();
/*
    @Shadow
    @Final
    @Mutable
    private LongSortedSet trackedPositions = new ConcurrentLongSortedSet();*/

}
