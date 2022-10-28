package calebzhou.rdi.core.server.mixin.server;

import calebzhou.rdi.core.server.utils.KRandomSource;
import net.minecraft.util.RandomSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;


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
