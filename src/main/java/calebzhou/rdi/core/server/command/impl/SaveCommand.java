package calebzhou.rdi.core.server.command.impl;

import calebzhou.rdi.core.server.RdiCoreServer;
import calebzhou.rdi.core.server.command.RdiCommand;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

public class SaveCommand extends RdiCommand {
    static {
		RdiCommand.register(new SaveCommand());
	}
	private SaveCommand() {
        super("SAVE","存档。");
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getExecution() {
        return baseArgBuilder.executes(c->{
            RdiCoreServer.getServer().saveEverything(true, true, true);
            c.getSource().sendSuccess(Component.literal("成功"),true);
            return 1;
        });
    }
}
