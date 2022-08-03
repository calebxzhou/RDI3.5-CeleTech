package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.command.BaseCommand;
import calebzhou.rdimc.celestech.command.RdiCommand;
import calebzhou.rdimc.celestech.constant.WorldConst;
import calebzhou.rdimc.celestech.utils.PlayerUtils;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;

public class SpawnCommand implements RdiCommand {
    @Override
    public String getName() {
        return "spawn";
    }
    public LiteralArgumentBuilder<CommandSourceStack> getExecution() {
        return Commands.literal(getName())
                .executes(context -> {
                    ServerPlayer player = context.getSource().getPlayer();
                    PlayerUtils.teleport(player, WorldConst.SPAWN_LOCA);
                    player.gameMode.changeGameModeForPlayer(GameType.SURVIVAL);
                    return 1;
                });
    }



}