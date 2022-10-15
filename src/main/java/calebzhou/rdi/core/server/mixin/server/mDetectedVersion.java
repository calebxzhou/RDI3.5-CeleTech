package calebzhou.rdi.core.server.mixin.server;

import calebzhou.rdi.core.server.constant.RdiSharedConstants;
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
public class mDetectedVersion {

	@Mutable
	@Shadow
	@Final
	private int protocolVersion;

	@Mutable
	@Shadow
	@Final
	private String name;

	@Inject(
            method = "<init>(Lcom/google/gson/JsonObject;)V",
            at=@At("TAIL")
    )
    private void changeVersion(JsonObject json, CallbackInfo ci){
		protocolVersion=(RdiSharedConstants.PROTOCOL_VERSION);
		name=(RdiSharedConstants.PROTOCOL_NAME);
    }
}
