package calebzhou.rdimc.celestech.mixin.server;

import calebzhou.rdimc.celestech.ServerStatus;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(MinecraftServer.class)
public class MixinLessLag {

    @Shadow private long timeReference;

    //设置服务器延迟等级
    @Inject(method = "Lnet/minecraft/server/MinecraftServer;runServer()V",
            at =@At(value = "INVOKE",target = "Lnet/minecraft/server/MinecraftServer;startTickMetrics()V"))
    private void setServerStatus(CallbackInfo ci){
        long milisBehind = Util.getMeasuringTimeMs()-timeReference;
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
