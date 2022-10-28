package calebzhou.rdi.core.server.mixin;

import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.NaturalSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Created by calebzhou on 2022-09-28,21:55.
 */

@Mixin(NaturalSpawner.class)
public interface AccessNaturalSpawner {
	@Accessor
	static MobCategory[] getSPAWNING_CATEGORIES() {
		return null;
	}

}
