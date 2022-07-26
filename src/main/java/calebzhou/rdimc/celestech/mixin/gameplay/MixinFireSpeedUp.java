package calebzhou.rdimc.celestech.mixin.gameplay;

import calebzhou.rdimc.celestech.RDICeleTech;
import net.minecraft.util.RandomSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Random;
import net.minecraft.world.level.block.FireBlock;

@Mixin(FireBlock.class)
public class MixinFireSpeedUp {
        //火加速
        @Overwrite
        private static int getFireTickDelay(RandomSource randomSource){
            return 2 + RDICeleTech.RANDOM.nextInt(10);
        }
}
