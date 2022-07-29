package calebzhou.rdimc.celestech.mixin.server;

import net.minecraft.world.level.NaturalSpawner;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(NaturalSpawner.class)
public class MixinSpawnHelper {
    /*@Shadow @Final @Mutable
    static int CHUNK_AREA = 1;*/
}
