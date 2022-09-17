package calebzhou.rdi.core.server.mixin.gameplay;

import net.minecraft.server.dedicated.DedicatedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(DedicatedServer.class)
public class mEnableCommandBlock {
    @Overwrite
    public boolean isCommandBlockEnabled() {
        return true;
    }
}
