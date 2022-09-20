package calebzhou.rdi.core.server.mixin.server;

import calebzhou.rdi.core.server.RdiMemoryStorage;
import calebzhou.rdi.core.server.model.RdiGeoLocation;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Created by calebzhou on 2022-09-20,22:01.
 */
@Mixin(targets = {"net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket$Action$1"})
public class mPlayerList {


	@Redirect(method = "write",at=@At(value = "INVOKE",target = "Lnet/minecraft/network/protocol/game/ClientboundPlayerInfoPacket$PlayerUpdate;getDisplayName()Lnet/minecraft/network/chat/Component;"))
	private Component setListDisplayNameWithProvince(ClientboundPlayerInfoPacket.PlayerUpdate instance){
		String name = instance.getProfile().getName();
		String uuid = instance.getProfile().getId().toString();
		RdiGeoLocation rdiGeoLocation = RdiMemoryStorage.pidGeoMap.get(uuid);
		String province = rdiGeoLocation.province.substring(0,2);
		String carrier = rdiGeoLocation.isp;
		String additionalDisplayName = " [%s/%s]".formatted(province,carrier);
		MutableComponent addiComponent = Component.empty().append(name).append(Component.literal(additionalDisplayName).withStyle(ChatFormatting.GRAY));
		return addiComponent;
	}
}
