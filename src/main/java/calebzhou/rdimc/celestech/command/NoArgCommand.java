package calebzhou.rdimc.celestech.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;

public abstract class NoArgCommand extends BaseCommand{
    public NoArgCommand(String name, int permissionLevel) {
        super(name, permissionLevel);
    }

    @Override
    public LiteralArgumentBuilder<ServerCommandSource> setExecution() {
        return builder.executes((context) -> execute(context.getSource()));
    }

    protected abstract int execute(ServerCommandSource source) throws CommandSyntaxException;
}
