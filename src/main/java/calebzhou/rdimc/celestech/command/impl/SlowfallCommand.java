package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.RDICeleTech;
import calebzhou.rdimc.celestech.command.BaseCommand;
import calebzhou.rdimc.celestech.constant.WorldConstants;
import calebzhou.rdimc.celestech.utils.PlayerUtils;
import calebzhou.rdimc.celestech.utils.TextUtils;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class SlowfallCommand extends BaseCommand {

    public SlowfallCommand(String command, int permissionLevel) {
        super(command, permissionLevel);
    }
    @Override
    public LiteralArgumentBuilder<ServerCommandSource> setExecution() {
        return builder.then(
                CommandManager.argument("level", IntegerArgumentType.integer())
                        .executes(context ->
                                execute(context.getSource(), IntegerArgumentType.getInteger(context, "level"))
                        )
        );
    }

    private int execute(ServerCommandSource source, int level) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayer();
        if(!PlayerUtils.getDimensionName(player).equals(WorldConstants.OVERWORLD)){
            TextUtils.sendChatMessage(player,"这个世界太沉了呀");
            return 1;
        }
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING,20*10,level+1));

        return Command.SINGLE_SUCCESS;
    }
}
