package calebzhou.rdi.core.server.mixin.mt;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.himeki.mcmtfabric.parallelised.fastutil.Int2ObjectConcurrentHashMap;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.EntityTickList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityTickList.class)
public abstract class EntityListMixin {
	@Shadow @Mutable
	private Int2ObjectMap<Entity> active = new Int2ObjectConcurrentHashMap<>();
	@Shadow @Mutable
	private Int2ObjectMap<Entity> passive = new Int2ObjectConcurrentHashMap<>();

    @Inject(method = "ensureActiveIsNotIterated", at = @At(value = "HEAD"), cancellable = true)
    private void notSafeAnyWay(CallbackInfo ci) {
        ci.cancel();
    }
}
