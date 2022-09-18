package calebzhou.rdi.core.server.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Created by calebzhou on 2022-09-18,21:11.
 */
@Mixin(ServerLoginPacketListenerImpl.class)
public interface AccessServerLoginPacketListenerImpl {
	@Accessor
	void setState(ServerLoginPacketListenerImpl.State state);
	@Accessor
	void setGameProfile(GameProfile profile);
}
