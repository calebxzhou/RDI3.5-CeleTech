package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.RDICeleTech;
import calebzhou.rdimc.celestech.command.BaseCommand;
import calebzhou.rdimc.celestech.command.NoArgCommand;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.constant.WorldConstants;
import calebzhou.rdimc.celestech.utils.*;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameMode;

import static calebzhou.rdimc.celestech.utils.TextUtils.sendChatMessage;

public class SpawnCommand extends NoArgCommand {
    public SpawnCommand(String name, int permissionLevel) {
        super(name, permissionLevel);
    }
    @Override
    protected void onExecute(ServerPlayerEntity player) {
        if(!PlayerUtils.getDimensionName(player).equals(WorldConstants.OVERWORLD)){
            TextUtils.sendChatMessage(player,"引力太强了!", MessageType.ERROR);
            return ;
        }
        PlayerUtils.teleport(player, WorldConstants.SPAWN_LOCA);
        ServerUtils.executeCommandOnServer("gamemode survival "+player.getEntityName());
    }


}