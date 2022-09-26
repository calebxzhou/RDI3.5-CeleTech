package calebzhou.rdi.core.server.command.impl;

import calebzhou.rdi.core.server.command.RdiCommand;
import calebzhou.rdi.core.server.utils.PlayerUtils;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.MessageArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

/**
 * Created by calebzhou on 2022-09-26,20:52.
 */

public class TellCommand extends RdiCommand {
	public TellCommand() {
		super("msg","私聊");
	}

	@Override
	public LiteralArgumentBuilder<CommandSourceStack> getExecution() {
		return baseArgBuilder.then(Commands.argument("player", EntityArgument.player())
				.then(Commands.argument("msg", MessageArgument.message()).executes(this::exec))).executes(context -> {
			PlayerUtils.sendMessageToCommandSource(context.getSource(),"请");
			return 1;
		});
	}

	private int exec(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		ServerPlayer fromPlayer = context.getSource().getPlayer();
		ServerPlayer toPlayer = EntityArgument.getPlayer(context, "player");
		Component msg = MessageArgument.getMessage(context, "msg");
		PlayerUtils.sendChatMessage(toPlayer,
				Component.literal("%s私聊您：".formatted(fromPlayer.getScoreboardName()))
						.append(msg));
		return 1;
	}
}
