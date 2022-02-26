package calebzhou.rdimc.celestech.mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
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
