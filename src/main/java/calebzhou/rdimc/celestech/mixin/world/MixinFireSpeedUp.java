package calebzhou.rdimc.celestech.mixin.world;

import calebzhou.rdimc.celestech.RDICeleTech;
import net.minecraft.block.FireBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Random;

@Mixin(FireBlock.class)
public class MixinFireSpeedUp {
        /**
         * @author
         * 火加速
         */
        @Overwrite
        private static int getFireTickDelay(Random random) {
            return 2 + RDICeleTech.RANDOM.nextInt(10);
        }
}
