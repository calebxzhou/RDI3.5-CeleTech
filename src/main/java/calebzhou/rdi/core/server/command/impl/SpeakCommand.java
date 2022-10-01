package calebzhou.rdi.core.server.command.impl;

import calebzhou.rdi.core.server.RdiCoreServer;
import calebzhou.rdi.core.server.RdiMemoryStorage;
import calebzhou.rdi.core.server.command.RdiCommand;
import calebzhou.rdi.core.server.utils.EncodingUtils;
import calebzhou.rdi.core.server.utils.PlayerUtils;
import calebzhou.rdi.core.server.utils.RdiHttpClient;
import calebzhou.rdi.core.server.utils.ServerUtils;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.MessageArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.apache.logging.log4j.core.jmx.Server;

import static calebzhou.rdi.core.server.RdiMemoryStorage.*;

/**
 * Created by calebzhou on 2022-09-19,11:58.
 */

public class SpeakCommand extends RdiCommand {
	public SpeakCommand() {
		super("speak","说话");
	}

	@Override
	public LiteralArgumentBuilder<CommandSourceStack> getExecution() {
		return baseArgBuilder.then(Commands.argument("msg", MessageArgument.message()).executes(context -> {
			ServerPlayer player = context.getSource().getPlayer();
			String pid = player.getStringUUID();

			MessageArgument.ChatMessage chatMessage = MessageArgument.getChatMessage(context, "msg");

			chatMessage.resolve(context.getSource(),playerChatMessage -> {
				String txt = playerChatMessage.signedContent().plain();
				if(pidToSpeakPlayersMap.containsKey(pid)){
					pidToSpeakPlayersMap.get(pid).forEach(pidToReceiveMsg->{
						ServerPlayer receiver = PlayerUtils.getPlayerByUuid(pidToReceiveMsg);
						if(receiver!=null){
							PlayerUtils.sendChatMessage(receiver,Component.literal(player.getScoreboardName()+"(岛内)：")
									.append(txt).withStyle(ChatFormatting.GOLD));
							RdiCoreServer.LOGGER.info("{}岛内说：{}",player.getScoreboardName(),txt);;
						}
					});
				}else{
					ServerUtils.broadcastChatMessage(Component.literal(player.getScoreboardName()+"：").append(txt));
					RdiCoreServer.LOGGER.info("{}说：{}",player.getScoreboardName(),txt);
				}
				RdiHttpClient.sendRequestAsyncResponseless("post","/mcs/record/chat", Pair.of("pid", pid),Pair.of("cont", EncodingUtils.getUTF8StringFromGBKString(txt)));
			});
			return 1;
		}));
	}
}
