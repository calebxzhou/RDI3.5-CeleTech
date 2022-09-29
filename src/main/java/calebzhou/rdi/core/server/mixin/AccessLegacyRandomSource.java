package calebzhou.rdi.core.server.mixin;

import net.minecraft.world.level.levelgen.LegacyRandomSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by calebzhou on 2022-09-29,9:37.
 */
@Mixin(LegacyRandomSource.class)
public interface AccessLegacyRandomSource {
	@Accessor
	AtomicLong getSeed();
}
