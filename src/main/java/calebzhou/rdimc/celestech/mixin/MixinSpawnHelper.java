package calebzhou.rdimc.celestech.mixin;

import net.minecraft.world.SpawnHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SpawnHelper.class)
public class MixinSpawnHelper {
    @Shadow @Final @Mutable
    static int CHUNK_AREA = 1;
}
