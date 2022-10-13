package calebzhou.rdi.core.server.mixin.server;

import net.minecraft.util.ThreadingDetector;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import org.apache.commons.lang3.RandomUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by calebzhou on 2022-10-06,20:28.
 */
@Mixin(LegacyRandomSource.class)
public class mLegacyRandomSource {


	@Shadow
	@Final
	private AtomicLong seed;

	@Overwrite
	public int next(int size) {
		long l = this.seed.get();
		long m = l * 25214903917L + 11L & 281474976710655L;
		 this.seed.set(l);
			return (int)(m >> 48 - size);
	}
}
