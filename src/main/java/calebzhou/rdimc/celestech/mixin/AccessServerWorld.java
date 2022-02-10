package calebzhou.rdimc.celestech.mixin;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.StructureLocator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ServerWorld.class)
public interface AccessServerWorld {
    @Accessor
    StructureLocator getStructureLocator();
}
