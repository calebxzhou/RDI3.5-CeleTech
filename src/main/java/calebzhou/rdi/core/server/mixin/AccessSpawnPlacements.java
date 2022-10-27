package calebzhou.rdi.core.server.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Map;

/**
 * Created  on 2022-10-25,20:50.
 */
@Mixin(SpawnPlacements.class)
public interface AccessSpawnPlacements {
	@Accessor
	static Map<EntityType<?>, SpawnPlacements.Data> getDATA_BY_TYPE(){return null;}
}
