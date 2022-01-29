package calebzhou.rdimc.celestech.command;

import calebzhou.rdimc.celestech.utils.ThreadPool;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public abstract class NoArgCommand extends BaseCommand{
    private boolean isAsync = false;

    public NoArgCommand(String name, int permissionLevel, boolean isAsync) {
        super(name, permissionLevel);
        this.isAsync = isAsync;
    }

    public NoArgCommand(String name, int permissionLevel) {
        super(name, permissionLevel);
    }

    @Override
    public LiteralArgumentBuilder<ServerCommandSource> setExecution() {
        return builder.executes((context) -> execute(context.getSource()));
    }

    private int execute(ServerCommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity fromPlayer = source.getPlayer();
        if(isAsync)
            ThreadPool.newThread(()->onExecute(fromPlayer));
        else
            onExecute(fromPlayer);

        return Command.SINGLE_SUCCESS;
    }

    protected abstract void onExecute(ServerPlayerEntity player);
}
