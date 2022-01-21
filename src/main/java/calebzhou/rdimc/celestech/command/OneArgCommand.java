package calebzhou.rdimc.celestech.command;

import calebzhou.rdimc.celestech.utils.ThreadPool;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public abstract class OneArgCommand extends BaseCommand {
    private boolean isAsync = false;

    public OneArgCommand(String name, int permissionLevel) {
        super(name, permissionLevel);
    }

    public OneArgCommand(String command, int permissionLevel, boolean isAsync) {
        super(command, permissionLevel);
        this.isAsync=isAsync;
    }
    @Override
    public LiteralArgumentBuilder<ServerCommandSource> setExecution() {
        return builder.then(CommandManager.argument("arg", StringArgumentType.string())
                        .executes(context -> execute(context.getSource(), StringArgumentType.getString(context, "arg"))));
    }

    private int execute(ServerCommandSource source, String arg) throws CommandSyntaxException {
        ServerPlayerEntity fromPlayer = source.getPlayer();
        if(isAsync)
            ThreadPool.newThread(()->onExecute(fromPlayer,arg));
        else
            onExecute(fromPlayer,arg);

        return Command.SINGLE_SUCCESS;
    }

    protected abstract void onExecute(ServerPlayerEntity player,String arg);
}
