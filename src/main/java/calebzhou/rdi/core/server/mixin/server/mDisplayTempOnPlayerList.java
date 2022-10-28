package calebzhou.rdi.core.server.mixin.server;

import calebzhou.rdi.core.server.utils.PlayerUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

/**
 * Created by calebzhou on 2022-09-20,22:01.
 */
@Mixin(ServerPlayer.class)
public class mDisplayTempOnPlayerList {
	@Overwrite
	public @Nullable Component getTabListDisplayName(){
		return PlayerUtils.getTabListDisplayName((Player) (Object) this);
	}

}
