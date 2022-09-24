package calebzhou.rdi.core.server.command.impl;

import calebzhou.rdi.core.server.RdiCoreServer;
import calebzhou.rdi.core.server.RdiSharedConstants;
import calebzhou.rdi.core.server.command.RdiCommand;
import calebzhou.rdi.core.server.utils.PlayerUtils;
import calebzhou.rdi.core.server.utils.ServerUtils;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

public class SpawnCommand extends RdiCommand {

	public SpawnCommand() {
        super( "spawn","回到主城");
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getExecution() {
        return baseArgBuilder
                .executes(context -> {
                    ServerPlayer player = context.getSource().getPlayer();
                    PlayerUtils.teleportToSpawn(player);
					PlayerUtils.sendChatMessage(player,PlayerUtils.RESPONSE_SUCCESS,"成功回到了主城！");
					return 1;
                });
    }



}
