package calebzhou.rdimc.celestech.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public abstract class BaseCommand {

    protected LiteralArgumentBuilder<ServerCommandSource> builder;

    public BaseCommand(String name, int permissionLevel) {
        this.builder = CommandManager.literal(name).requires(source -> source.hasPermissionLevel(permissionLevel));
    }

    public LiteralArgumentBuilder<ServerCommandSource> getBuilder() {
        return builder;
    }


    public LiteralArgumentBuilder<ServerCommandSource> setExecution() {
        return null;
    }

}
