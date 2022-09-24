package calebzhou.rdi.core.server.command.impl;

import calebzhou.rdi.core.server.RdiEvents;
import calebzhou.rdi.core.server.RdiMemoryStorage;
import calebzhou.rdi.core.server.command.RdiCommand;
import calebzhou.rdi.core.server.constant.ColorConst;
import calebzhou.rdi.core.server.utils.PlayerUtils;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

import static calebzhou.rdi.core.server.utils.PlayerUtils.*;

public class HelpCommand extends RdiCommand {
	public HelpCommand( ) {
        super("rdi-help");
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getExecution() {
        return baseArgBuilder.executes(context -> {
			ServerPlayer player = context.getSource().getPlayer();
			sendChatMessage(player, ColorConst.BRIGHT_GREEN+"=====RDI帮助菜单=====");
			RdiMemoryStorage.commandSet.parallelStream().forEach(command -> {
				sendChatMessage(player,"/%s：%s".formatted(command.getName(),command.getDescription()));
			});
			return 1;
		});
    }
}
