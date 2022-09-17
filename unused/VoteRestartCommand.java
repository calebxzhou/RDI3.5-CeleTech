package calebzhou.rdi.celestech.command.impl;

import calebzhou.rdi.celestech.command.RdiCommand;
import calebzhou.rdi.celestech.constant.WorldConst;
import calebzhou.rdi.celestech.utils.PlayerUtils;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;

public class VoteRestartCommand extends RdiCommand {
    @Override
    public String getName() {
        return "vote-restart";
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getExecution() {
        return baseArgBuilder
                .executes(context -> exec(context.getSource().getPlayer()));
    }

    private int exec(ServerPlayer player) {
        return 1;
    }
}
