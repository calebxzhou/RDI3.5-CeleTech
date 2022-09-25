package calebzhou.rdi.core.server.mixin.server;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.LastSeenMessagesValidator;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by calebzhou on 2022-09-25,12:57.
 */
@Mixin(ServerGamePacketListenerImpl.class)
public class mNoChatValidation {
	@Overwrite
	private void handleValidationFailure(Set<LastSeenMessagesValidator.ErrorCondition>
													 errorConditions) {}
}
