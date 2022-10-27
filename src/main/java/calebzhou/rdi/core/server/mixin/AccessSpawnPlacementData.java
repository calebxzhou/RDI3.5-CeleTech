package calebzhou.rdi.core.server.mixin;

import net.minecraft.world.entity.SpawnPlacements;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Created  on 2022-10-25,20:54.
 */
@Mixin(SpawnPlacements.Data.class)
public interface AccessSpawnPlacementData {
	@Accessor
	SpawnPlacements.SpawnPredicate getPredicate();
}
