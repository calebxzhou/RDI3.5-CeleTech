package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.command.RdiCommand;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.constant.WorldConst;
import calebzhou.rdimc.celestech.utils.PlayerUtils;
import calebzhou.rdimc.celestech.utils.ServerUtils;
import calebzhou.rdimc.celestech.utils.TextUtils;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;

public class SpawnCommand extends RdiCommand {
    public SpawnCommand(   ) {
        super( "spawn");
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getExecution() {
        return baseArgBuilder
                .executes(context -> {
                    ServerPlayer player = context.getSource().getPlayer();
                    if(player.experienceLevel<3){
                        TextUtils.sendChatMessage(player, MessageType.ERROR,"需要达到3级经验才能返回主城！");
                        return 1;
                    }
                    PlayerUtils.teleport(player, WorldConst.SPAWN_LOCA);
                    ServerUtils.executeCommandOnServer("gamemode survival "+player.getScoreboardName());
                    return 1;
                });
    }



}