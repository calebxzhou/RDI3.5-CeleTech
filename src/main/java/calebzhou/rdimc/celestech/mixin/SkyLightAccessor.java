package calebzhou.rdimc.celestech.mixin;

import net.minecraft.world.chunk.light.SkyLightStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(SkyLightStorage.class)
public interface SkyLightAccessor {
    @Invoker("getLight")
    public int getLight(long blockPos);
}
