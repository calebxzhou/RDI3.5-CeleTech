package calebzhou.rdi.core.server.command.impl;

import calebzhou.rdi.core.server.command.RdiCommand;
import calebzhou.rdi.core.server.utils.ServerUtils;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.MessageArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.apache.logging.log4j.core.jmx.Server;

/**
 * Created by calebzhou on 2022-09-19,11:58.
 */

public class SpeakCommand extends RdiCommand {
	public SpeakCommand() {
		super("speak","说话");
	}

	@Override
	public LiteralArgumentBuilder<CommandSourceStack> getExecution() {
		return baseArgBuilder.then(Commands.argument("msg", MessageArgument.message()).executes(context -> {
			ServerPlayer player = context.getSource().getPlayer();
			MessageArgument.ChatMessage chatMessage = MessageArgument.getChatMessage(context, "msg");
			chatMessage.resolve(context.getSource(),playerChatMessage -> {
				ServerUtils.broadcastChatMessage(
						Component.literal(player.getScoreboardName()+"：")
								.append(playerChatMessage.signedContent().plain()));
			});
			return 1;
		}));
	}
}
