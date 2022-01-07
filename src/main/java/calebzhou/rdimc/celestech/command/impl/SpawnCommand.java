package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.RDICeleTech;
import calebzhou.rdimc.celestech.command.BaseCommand;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.constant.WorldConstants;
import calebzhou.rdimc.celestech.utils.*;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import static calebzhou.rdimc.celestech.utils.TextUtils.sendChatMessage;

public class SpawnCommand extends BaseCommand {
    public SpawnCommand(String name, int permissionLevel) {
        super(name, permissionLevel);
    }

    @Override
    public LiteralArgumentBuilder<ServerCommandSource> setExecution() {
        return builder.executes((context) -> execute(context.getSource()));
    }

    private int execute(ServerCommandSource source) throws CommandSyntaxException {

        ServerPlayerEntity player = source.getPlayer();
        if(!PlayerUtils.getDimensionName(player).equals(WorldConstants.OVERWORLD)){
            TextUtils.sendChatMessage(player,"您必须在主世界使用本指令!", MessageType.ERROR);
            return 1;
        }
        PlayerUtils.teleport(player, WorldConstants.SPAWN_LOCA);
        return 1;
    }


}