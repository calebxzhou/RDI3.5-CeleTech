package calebzhou.rdimc.celestech.mixin;

import net.minecraft.block.FireBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Random;

@Mixin(FireBlock.class)
public class FireBlockMixin {
        /**
         * @author
         */
        @Overwrite
        private static int getFireTickDelay(Random random) {
            return 5 + random.nextInt(10);
        }
}
