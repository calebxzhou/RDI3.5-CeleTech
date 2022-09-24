package calebzhou.rdi.core.server.mixin.server;

import calebzhou.rdi.core.server.RdiSharedConstants;
import net.minecraft.obfuscate.DontObfuscate;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.dedicated.DedicatedServerProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

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
		return "rdi-core";
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
