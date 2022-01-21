package calebzhou.rdimc.celestech.command;

import calebzhou.rdimc.celestech.RDICeleTech;
import calebzhou.rdimc.celestech.constant.ColorConstants;
import calebzhou.rdimc.celestech.utils.ServerUtils;
import calebzhou.rdimc.celestech.utils.ThreadPool;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;

import java.util.UUID;

import static calebzhou.rdimc.celestech.utils.TextUtils.*;

public abstract class PlayerArgCommand extends BaseCommand {

    public PlayerArgCommand(String command, int permissionLevel) {
        super(command, permissionLevel);
    }
    public static final SuggestionProvider<ServerCommandSource> SUGGESTION_PROVIDER = (context, builder) -> CommandSource.suggestMatching(ServerUtils.getOnlinePlayerList(), builder);
    @Override
    public LiteralArgumentBuilder<ServerCommandSource> setExecution() {
        return builder.then(CommandManager.argument("targetPlayer", EntityArgumentType.players()).suggests(SUGGESTION_PROVIDER)
                        .executes(context -> execute(context.getSource(), EntityArgumentType.getPlayer(context, "targetPlayer"))));
    }

    private int execute(ServerCommandSource source, ServerPlayerEntity toPlayer) throws CommandSyntaxException {
        ServerPlayerEntity fromPlayer = source.getPlayer();
        onExecute(fromPlayer,toPlayer);

        return Command.SINGLE_SUCCESS;
    }

    protected abstract void onExecute(ServerPlayerEntity fromPlayer,ServerPlayerEntity toPlayer);
}
