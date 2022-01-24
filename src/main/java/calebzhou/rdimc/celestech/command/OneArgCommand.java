package calebzhou.rdimc.celestech.command;

import calebzhou.rdimc.celestech.utils.ThreadPool;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.MessageArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

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
        return builder.then(CommandManager.argument("arg", MessageArgumentType.message())
                        .executes(context -> execute(context.getSource(), MessageArgumentType.getMessage(context, "arg"))));
    }

    private int execute(ServerCommandSource source, Text arg) throws CommandSyntaxException {
        ServerPlayerEntity fromPlayer = source.getPlayer();
        String args= arg.getString();
        if(isAsync)
            ThreadPool.newThread(()->onExecute(fromPlayer,args));
        else
            onExecute(fromPlayer,args);

        return Command.SINGLE_SUCCESS;
    }

    protected abstract void onExecute(ServerPlayerEntity player,String arg);
}
