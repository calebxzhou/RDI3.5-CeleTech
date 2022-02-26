package calebzhou.rdimc.celestech.mixin.server;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ChunkTicketManager;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.function.BooleanSupplier;

import static calebzhou.rdimc.celestech.RDICeleTech.LOGGER;

@Mixin(MinecraftServer.class)
public abstract class MixinNeverCrash {
//用try-catch包起来服务器运行主体，no crash
    @Redirect(method = "Lnet/minecraft/server/MinecraftServer;runServer()V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;tick(Ljava/util/function/BooleanSupplier;)V"))
    private void ticks(MinecraftServer instance, BooleanSupplier shouldKeepTicking){
        try{
            ((MinecraftServer)(Object)this).tick(shouldKeepTicking);
        }catch (Throwable e){
            LOGGER.error(e.getMessage());
        }
    }

    private ServerWorld world;
    @Inject(method = "Lnet/minecraft/server/MinecraftServer;tickWorlds(Ljava/util/function/BooleanSupplier;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;tick(Ljava/util/function/BooleanSupplier;)V"),
            locals = LocalCapture.CAPTURE_FAILSOFT)
    private void tickworldInj(BooleanSupplier shouldKeepTicking, CallbackInfo ci, Iterator var2,ServerWorld serverWorld){
        this.world=serverWorld;
    }
    @Redirect(
            method = "Lnet/minecraft/server/MinecraftServer;tickWorlds(Ljava/util/function/BooleanSupplier;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;tick(Ljava/util/function/BooleanSupplier;)V"))
    private void tickworld(ServerWorld instance, BooleanSupplier shouldKeepTicking){
        try{
            this.world.tick(shouldKeepTicking);
        }catch (Throwable e){
            LOGGER.error(e.getMessage());
        }
    }
}
@Mixin(ServerChunkManager.class)
class AntiChunkCrash{
    @Shadow
    @Final
    private ChunkTicketManager ticketManager;
    @Shadow @Final
    public ThreadedAnvilChunkStorage threadedAnvilChunkStorage;
    @Shadow @Final private static Logger LOGGER;

    @Redirect(method = "Lnet/minecraft/server/world/ServerChunkManager;tick()Z",
            at=@At(value = "INVOKE",target = "Lnet/minecraft/server/world/ChunkTicketManager;tick(Lnet/minecraft/server/world/ThreadedAnvilChunkStorage;)Z"))
    private boolean nocrashTick(ChunkTicketManager instance, ThreadedAnvilChunkStorage chunkStorage){
        try{
            boolean tick =  this.ticketManager.tick(this.threadedAnvilChunkStorage);
            return tick;
        }catch (Throwable t){
            LogManager.getLogger().error(t.toString());
        }
        return false;
    }

}