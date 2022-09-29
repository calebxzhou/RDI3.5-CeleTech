package calebzhou.rdi.core.server.mixin.server;

import calebzhou.rdi.core.server.RdiMemoryStorage;
import calebzhou.rdi.core.server.model.RdiWeather;
import calebzhou.rdi.core.server.utils.PlayerUtils;
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
		return PlayerUtils.getTabListDisplayName((Player) (Object) this);
	}

}
