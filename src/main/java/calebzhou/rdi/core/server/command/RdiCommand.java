package calebzhou.rdi.core.server.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public abstract class RdiCommand {
    private String commandName;

    protected LiteralArgumentBuilder<CommandSourceStack> baseArgBuilder;
    public RdiCommand(String commandName){
        this.commandName=commandName;
        this.baseArgBuilder= Commands.literal(commandName);
    }
    public String getName() {
        return commandName;
    }
    public abstract LiteralArgumentBuilder<CommandSourceStack> getExecution();
}
