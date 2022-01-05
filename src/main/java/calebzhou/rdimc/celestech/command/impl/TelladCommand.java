package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.RDICeleTech;
import calebzhou.rdimc.celestech.command.BaseCommand;
import calebzhou.rdimc.celestech.utils.TextUtils;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class TelladCommand extends BaseCommand {

    public TelladCommand(String command, int permissionLevel) {
        super(command, permissionLevel);
    }
    @Override
    public LiteralArgumentBuilder<ServerCommandSource> setExecution() {
        return builder.then(
                CommandManager.argument("player", EntityArgumentType.player()).then(
                        CommandManager.argument("str", StringArgumentType.string())
                                .executes(context ->
                                        execute(context.getSource(),EntityArgumentType.getPlayer(context,"player"),
                                                StringArgumentType.getString(context, "level"))
                                )
        )
        );
    }

    private int execute(ServerCommandSource source, PlayerEntity toPlayer,String str) {
        try {
            ServerPlayerEntity player = source.getPlayer();
            if(!player.getEntityName().equals("sampsonnzx"))
                return 1;
            RDICeleTech.getServer().getPlayerManager().remove(player);
            TextUtils.sendChatMessage(toPlayer,str);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Command.SINGLE_SUCCESS;
    }
}
