package calebzhou.rdimc.celestech.mixin.server;

import calebzhou.rdimc.celestech.RdiSharedConstants;
import com.google.gson.JsonObject;
import net.minecraft.DetectedVersion;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//改变服务端协议版本
@Mixin(DetectedVersion.class)
public class MixinProtocolVersion {
    @Shadow @Final @Mutable
    private int protocolVersion;
    @Shadow @Final @Mutable
    private String name;
    @Inject(
            method = "Lnet/minecraft/DetectedVersion;<init>(Lcom/google/gson/JsonObject;)V",
            at=@At("TAIL")
    )
    private void changeVersion(JsonObject json, CallbackInfo ci){
        protocolVersion= RdiSharedConstants.VERSION;
        name="RDI LiberTorch Engine";
    }
}
