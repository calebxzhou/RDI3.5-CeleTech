package calebzhou.rdi.core.server.command.impl;

import calebzhou.rdi.core.server.RdiCommandConfirmer;
import calebzhou.rdi.core.server.RdiMemoryStorage;
import calebzhou.rdi.core.server.command.RdiCommand;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.Consumer;

import static calebzhou.rdi.core.server.utils.PlayerUtils.*;

/**
 * Created by calebzhou on 2022-09-18,9:27.
 */
public class RdiConfirmCommand extends RdiCommand {
	public RdiConfirmCommand() {
		super("rdi-confirm","确认执行指令。");
	}

	@Override
	public LiteralArgumentBuilder<CommandSourceStack> getExecution() {
		return baseArgBuilder.then(Commands.argument("confirm-id", StringArgumentType.string()).executes(RdiCommandConfirmer::onCommandExecute));
	}

}
