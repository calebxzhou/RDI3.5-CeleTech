package calebzhou.rdi.core.server.command.impl;

import calebzhou.rdi.core.server.RdiMemoryStorage;
import calebzhou.rdi.core.server.command.RdiCommand;
import calebzhou.rdi.core.server.model.RdiPlayerLocation;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

/**
 * Created by calebzhou on 2022-09-23,20:30.
 */
public class BackCommand extends RdiCommand {
	public BackCommand() {
		super("back");
	}

	@Override
	public LiteralArgumentBuilder<CommandSourceStack> getExecution() {
		return baseArgBuilder.executes(context -> {
			ServerPlayer player = context.getSource().getPlayer();
			RdiPlayerLocation pos = RdiMemoryStorage.pidBackPos.get(player.getStringUUID());
return 1;
		});
	}
}
