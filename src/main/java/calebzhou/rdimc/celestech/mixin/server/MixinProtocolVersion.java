package calebzhou.rdimc.celestech.mixin.server;

import calebzhou.rdimc.celestech.RDICeleTech;
import com.google.gson.JsonObject;
import net.minecraft.MinecraftVersion;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftVersion.class)
public class MixinProtocolVersion {
    @Shadow @Final @Mutable
    private int protocolVersion;
    @Shadow @Final @Mutable
    private String name;
    @Inject(
            method = "Lnet/minecraft/MinecraftVersion;<init>(Lcom/google/gson/JsonObject;)V",
            at=@At("TAIL")
    )
    private void asdasdcx(JsonObject json, CallbackInfo ci){
        protocolVersion=RDICeleTech.VERSION;
        name="RDI LiberaTorch Engine 3";
    }
}
