package calebzhou.rdi.core.server.mixin.server;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

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

}
