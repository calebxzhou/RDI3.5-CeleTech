package calebzhou.rdi.core.server.mixin.server;

import calebzhou.rdi.core.server.RdiMemoryStorage;
import calebzhou.rdi.core.server.model.RdiWeather;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Created by calebzhou on 2022-09-20,22:01.
 */
//@Mixin(targets = {"net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket$Action$1"})
@Mixin(ServerPlayer.class)
public class mDisplayTempOnPlayerList {
	@Overwrite
	public @Nullable Component getTabListDisplayName(){
		Player player = (Player)(Object)this;
		RdiWeather rdiWeather = RdiMemoryStorage.pidWeatherMap.get(player.getStringUUID());
		if(rdiWeather==null)
			return  null;
		double temperature = rdiWeather.temperature;
		ChatFormatting color = ChatFormatting.WHITE;
		if(temperature>40)
			color = ChatFormatting.DARK_RED;
		else if(temperature>30)
			color = ChatFormatting.RED;
		else if(temperature>20)
			color = ChatFormatting.GOLD;
		else if(temperature>10)
			color = ChatFormatting.GREEN;
		else if(temperature>0)
			color = ChatFormatting.AQUA;
		else if(temperature>-10)
			color = ChatFormatting.BLUE;
		else if(temperature<=-10)
			color = ChatFormatting.DARK_BLUE;

		String additionalDisplayName = " %s℃ ".formatted(Math.round(temperature));
		return Component.empty().append(player.getScoreboardName()).append(Component.literal(additionalDisplayName).withStyle(color));
	}

//在tab列表显示温度
/*	@Redirect(method = "write",at=@At(value = "INVOKE",target = "Lnet/minecraft/network/protocol/game/ClientboundPlayerInfoPacket$PlayerUpdate;getDisplayName()Lnet/minecraft/network/chat/Component;"))
	private Component setListDisplayNameWithProvince(ClientboundPlayerInfoPacket.PlayerUpdate instance){
		String name = instance.getProfile().getName();
		String uuid = instance.getProfile().getId().toString();
		*//*RdiGeoLocation rdiGeoLocation = RdiMemoryStorage.pidGeoMap.get(uuid);
		String province = rdiGeoLocation.province.substring(0,2);
		String carrier = rdiGeoLocation.isp;*//*
		RdiWeather rdiWeather = RdiMemoryStorage.pidWeatherMap.get(uuid);
		if(rdiWeather==null){
			return Component.empty().append(name);
		}
		double temperature = rdiWeather.temperature;
		ChatFormatting color = ChatFormatting.WHITE;
		if(temperature>40)
			color = ChatFormatting.DARK_RED;
		else if(temperature>30)
			color = ChatFormatting.RED;
		else if(temperature>20)
			color = ChatFormatting.GOLD;
		else if(temperature>10)
			color = ChatFormatting.GREEN;
		else if(temperature>0)
			color = ChatFormatting.AQUA;
		else if(temperature>-10)
			color = ChatFormatting.BLUE;
		else if(temperature<=-10)
			color = ChatFormatting.DARK_BLUE;

		String additionalDisplayName = " %s℃ ".formatted(Math.round(temperature));
		MutableComponent addiComponent = Component.empty().append(name).append(Component.literal(additionalDisplayName).withStyle(color));
		return addiComponent;
	}*/
}
