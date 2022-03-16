package calebzhou.rdimc.celestech.mixin.server;

import net.minecraft.world.level.NaturalSpawner;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(NaturalSpawner.class)
public class MixinSpawnHelper {
    /*@Shadow @Final @Mutable
    static int CHUNK_AREA = 1;*/
}
