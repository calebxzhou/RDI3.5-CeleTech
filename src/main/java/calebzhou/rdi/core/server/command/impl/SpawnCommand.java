package calebzhou.rdi.core.server.command.impl;

import calebzhou.rdi.core.server.RdiCoreServer;
import calebzhou.rdi.core.server.RdiSharedConstants;
import calebzhou.rdi.core.server.command.RdiCommand;
import calebzhou.rdi.core.server.constant.WorldConst;
import calebzhou.rdi.core.server.utils.PlayerUtils;
import calebzhou.rdi.core.server.utils.ServerUtils;
import calebzhou.rdi.core.server.utils.TextUtils;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

public class SpawnCommand extends RdiCommand {
    public SpawnCommand(   ) {
        super( "maintown");
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getExecution() {
        return baseArgBuilder
                .executes(context -> {
                    ServerPlayer player = context.getSource().getPlayer();
                    if(player.experienceLevel<3){
                        PlayerUtils.sendChatMessage(player, PlayerUtils.RESPONSE_ERROR,"需要达到3级经验才能返回主城！");
                        return 1;
                    }
                    PlayerUtils.teleport(player, RdiCoreServer.getServer().overworld(),RdiSharedConstants.SPAWN_LOCATION);
                    ServerUtils.executeCommandOnServer("gamemode survival "+player.getScoreboardName());
                    return 1;
                });
    }



}
