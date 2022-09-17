package calebzhou.rdi.core.server.mixin.gameplay;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(MinecraftServer.class)
public class mAllowFlying {
    @Overwrite
    public boolean isFlightAllowed() {
        return true;
    }
}
