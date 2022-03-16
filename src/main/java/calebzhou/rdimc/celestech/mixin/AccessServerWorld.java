package calebzhou.rdimc.celestech.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.structure.StructureCheck;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ServerLevel.class)
public interface AccessServerWorld {
    @Accessor
    StructureCheck getStructureLocator();
}
