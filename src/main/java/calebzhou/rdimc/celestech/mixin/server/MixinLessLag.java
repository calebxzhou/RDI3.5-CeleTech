package calebzhou.rdimc.celestech.mixin.server;

import calebzhou.rdimc.celestech.ServerStatus;
import net.minecraft.Util;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(MinecraftServer.class)
public class MixinLessLag {

    @Shadow private long nextTickTime;

    //设置服务器延迟等级
    @Inject(method = "runServer()V",
            at =@At(value = "INVOKE",target = "Lnet/minecraft/server/MinecraftServer;startMetricsRecordingTick()V"))
    private void setServerStatus(CallbackInfo ci){
        long milisBehind = Util.getMillis()-nextTickTime;
        if(milisBehind>4000){
            ServerStatus.flag=(ServerStatus.WORST);
        }else if(milisBehind>2200){
            ServerStatus.flag=(ServerStatus.BAD);
        }else if(milisBehind>1500){
            ServerStatus.flag=(ServerStatus.GOOD);
        }else
            ServerStatus.flag=(ServerStatus.BEST);
    }



}
