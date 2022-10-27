package calebzhou.rdi.core.server.mixin.server;

import calebzhou.rdi.core.server.utils.KRandomSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.*;


/**
 * Created  on 2022-10-25,21:40.
 */
@Mixin(RandomSource.class)
public interface mUseRdiRandomSource {
	@Overwrite
	static RandomSource create(long seed) {
		return KRandomSource.INSTANCE;
	}
}
