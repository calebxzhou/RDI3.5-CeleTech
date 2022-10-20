package calebzhou.rdi.core.server.mixin.server;

import calebzhou.rdi.core.server.misc.ServerLaggingStatus;
import net.minecraft.Util;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Created by calebzhou on 2022-09-25,13:47.
 */
@Mixin(MinecraftServer.class)
public class mSetServerLaggingStatus {
	@Shadow
	private long nextTickTime;
	//设置服务器延迟等级
	@Inject(method = "runServer()V",
			at =@At(value = "INVOKE",target = "Lnet/minecraft/server/MinecraftServer;startMetricsRecordingTick()V"))
	private void setServerStatus(CallbackInfo ci){
		//现在的tick时间减去下次tick时间
		long milisBehind = Util.getMillis()-nextTickTime;
		ServerLaggingStatus.updateMilliSecondsBehind(milisBehind);;

	}
}
