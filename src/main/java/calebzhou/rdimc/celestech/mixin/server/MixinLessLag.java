package calebzhou.rdimc.celestech.mixin.server;

import calebzhou.rdimc.celestech.ServerStatus;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(MinecraftServer.class)
public class MixinLessLag {
    @Inject(method = "Lnet/minecraft/server/MinecraftServer;runServer()V",
            locals = LocalCapture.CAPTURE_FAILSOFT,
            at =@At(value = "INVOKE",target = "Lnet/minecraft/server/MinecraftServer;startTickMetrics()V"))
    private void setServerStatus(CallbackInfo ci,long milisBehind){
        if(milisBehind>4000){
            ServerStatus.setStatus(ServerStatus.WORST);
        }else if(milisBehind>2200){
            ServerStatus.setStatus(ServerStatus.BAD);
        }else if(milisBehind>1500){
            ServerStatus.setStatus(ServerStatus.GOOD);
        }else
            ServerStatus.setStatus(ServerStatus.BEST);
    }

}
