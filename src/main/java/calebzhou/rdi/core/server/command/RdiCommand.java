package calebzhou.rdi.core.server.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public abstract class RdiCommand {
    private final String commandName;
    private final String description;

    protected LiteralArgumentBuilder<CommandSourceStack> baseArgBuilder;

    protected RdiCommand(String commandName){
        this.commandName=commandName;
		this.description="";
        this.baseArgBuilder= Commands.literal(commandName);
    }
	protected RdiCommand(String commandName,String description){
        this.commandName=commandName;
		this.description=description;
        this.baseArgBuilder= Commands.literal(commandName);
    }
    public String getName() {
        return commandName;
    }

	public String getDescription() {
		return description;
	}

	public abstract LiteralArgumentBuilder<CommandSourceStack> getExecution();

}
