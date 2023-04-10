package calebzhou.rdi.core.server.mixin.server;

import calebzhou.rdi.core.server.constant.RdiSharedConstants;
import calebzhou.rdi.core.server.ticking.ServerTicking;
import calebzhou.rdi.core.server.utils.ServerUtils;
import net.minecraft.obfuscate.DontObfuscate;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.dedicated.DedicatedServerProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.BooleanSupplier;

/**
 * Created by calebzhou on 2022-09-18,21:03.
 */
@Mixin(MinecraftServer.class)
public class mMinecraftServer {
	//停用正版验证
	@Overwrite
	public boolean usesAuthentication() {
		return false;
	}
	//允许飞行
	@Overwrite
	public boolean isFlightAllowed() {
		return true;
	}

	@DontObfuscate
	@Overwrite(remap = false)
	public String getServerModName(){
		return RdiSharedConstants.SERVER_TYPE;
	}

	@Redirect(method = "runServer",at  =  @At(value = "INVOKE",target = "Lnet/minecraft/server/MinecraftServer;tickServer(Ljava/util/function/BooleanSupplier;)V"))
	private void execTickServer(MinecraftServer server, BooleanSupplier hasTimeLeft){

		try{
			server.tickServer(hasTimeLeft);
			//ServerTicking.tick(server,hasTimeLeft);
		}catch (Throwable e){
			e.printStackTrace();
			ServerUtils.broadcastChatMessage("tick server错误"+ e +e.getCause());
		}
	}
}
@Mixin(DedicatedServer.class)
abstract
class mDedicatedServer{
	@Shadow
	public abstract DedicatedServerProperties getProperties();

	//调试模式允许暂停60秒以上
	@Overwrite
	public long getMaxTickLength() {
		return RdiSharedConstants.DEBUG?0:getProperties().maxTickTime;
	}

}
