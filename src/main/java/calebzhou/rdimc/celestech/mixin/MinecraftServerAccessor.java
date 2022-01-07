package calebzhou.rdimc.celestech.mixin;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MinecraftServer.class)
public interface MinecraftServerAccessor {
    @Invoker("setupServer")
    boolean setupServer();
    @Invoker("shouldKeepTicking")
    boolean shouldKeepTicking();
    @Invoker("startTickMetrics")
    void startTickMetrics();
    @Invoker("runTasksTillTickEnd")
    void runTasksTillTickEnd();
    @Invoker("endTickMetrics")
    void endTickMetrics();
}
