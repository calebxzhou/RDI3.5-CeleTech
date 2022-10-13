package calebzhou.rdi.core.server.mixin.gameplay;

import calebzhou.rdi.core.server.RdiCoreServer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.FireBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(FireBlock.class)
public class mFireSpeedUp {
        //火加速
        @Overwrite
        private static int getFireTickDelay(RandomSource randomSource){
            return 2 + RdiCoreServer.RANDOM.nextInt(10);
        }
}