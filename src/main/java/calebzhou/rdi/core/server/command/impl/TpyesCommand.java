package calebzhou.rdi.core.server.command.impl;

import calebzhou.rdi.core.server.RdiMemoryStorage;
import calebzhou.rdi.core.server.command.RdiCommand;
import calebzhou.rdi.core.server.utils.PlayerUtils;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import org.apache.commons.lang3.StringUtils;

import static calebzhou.rdi.core.server.utils.PlayerUtils.RESPONSE_ERROR;
import static calebzhou.rdi.core.server.utils.PlayerUtils.sendChatMessage;


public class TpyesCommand extends RdiCommand {
	static {
		RdiCommand.register(new TpyesCommand());
	}
    private TpyesCommand() {
        super("tpyes","接受传送请求");
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getExecution() {
        return baseArgBuilder.executes(context -> {
			ServerPlayer toPlayer = context.getSource().getPlayer();
			if(toPlayer.experienceLevel<3){
				sendChatMessage(toPlayer, RESPONSE_ERROR,"经验不足,您需要3级经验.");
				return 1;
			}

			String fromPlayerId = RdiMemoryStorage.tpaMap.get(toPlayer.getStringUUID());
			if(StringUtils.isEmpty(fromPlayerId)){
				sendChatMessage(toPlayer,RESPONSE_ERROR, "找不到传送请求....");
				return 1;
			}
			ServerPlayer fromPlayer = PlayerUtils.getPlayerByUuid(fromPlayerId);
			if(fromPlayer == null){
				sendChatMessage(toPlayer,RESPONSE_ERROR, "玩家不在线！");
				return 1;
			}
			sendChatMessage(toPlayer,"正在传送..");
			sendChatMessage(fromPlayer,"正在传送..");
			PlayerUtils.teleport(fromPlayer,toPlayer);
			toPlayer.experienceLevel -= 3;

			RdiMemoryStorage.tpaMap.remove(toPlayer.getStringUUID());
			return 1;
		});
    }
}
