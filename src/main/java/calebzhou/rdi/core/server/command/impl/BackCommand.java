package calebzhou.rdi.core.server.command.impl;

import calebzhou.rdi.core.server.RdiMemoryStorage;
import calebzhou.rdi.core.server.RdiPlayerLocationRecorder;
import calebzhou.rdi.core.server.command.RdiCommand;
import calebzhou.rdi.core.server.model.RdiPlayerLocation;
import calebzhou.rdi.core.server.utils.PlayerUtils;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

/**
 * Created by calebzhou on 2022-09-23,20:30.
 */
public class BackCommand extends RdiCommand {
	public BackCommand() {
		super("back","返回上次传送位置");
	}

	@Override
	public LiteralArgumentBuilder<CommandSourceStack> getExecution() {
		return baseArgBuilder.executes(context -> {
			ServerPlayer player = context.getSource().getPlayer();
			RdiPlayerLocation pos = RdiPlayerLocationRecorder.getLocation(player);
			if(pos == null){
				PlayerUtils.sendChatMessage(player,PlayerUtils.RESPONSE_ERROR,"您没有传送记录！");
				return 1;
			}
			if(player.experienceLevel<3){
				PlayerUtils.sendChatMessage(player,PlayerUtils.RESPONSE_ERROR,"需要3级经验才能返回上次记录的传送位置！");
				return 1;
			}
			PlayerUtils.teleport(player,pos);
			RdiPlayerLocationRecorder.remove(player);
			PlayerUtils.sendChatMessage(player,PlayerUtils.RESPONSE_SUCCESS);
			return 1;
		});
	}
}
