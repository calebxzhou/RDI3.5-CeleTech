package calebzhou.rdi.core.server.command.impl;

import calebzhou.rdi.core.server.command.RdiCommand;
import calebzhou.rdi.core.server.utils.PlayerUtils;
import calebzhou.rdi.core.server.utils.WorldUtils;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Blocks;

import static calebzhou.rdi.core.server.utils.PlayerUtils.RESPONSE_ERROR;
import static calebzhou.rdi.core.server.utils.PlayerUtils.sendChatMessage;

/**
 * Created by calebzhou on 2022-09-18,16:11.
 */
public class MeltObsidianCommand extends RdiCommand {
	public MeltObsidianCommand() {
		super("melt-obsidian","熔化黑曜石");
	}

	@Override
	public LiteralArgumentBuilder<CommandSourceStack> getExecution() {
		return baseArgBuilder.executes(context -> {
			ServerPlayer player = context.getSource().getPlayer();
			BlockPos posLook = PlayerUtils.getPlayerLookingBlockPosition(player, false);
			if(Blocks.OBSIDIAN == player.getLevel().getBlockState(posLook).getBlock()){
				WorldUtils.placeBlock(player.getLevel(),posLook,Blocks.LAVA);
			}else{
				sendChatMessage(player,RESPONSE_ERROR,"必须对准黑曜石");
			}
			return 1;
		});
	}
}
