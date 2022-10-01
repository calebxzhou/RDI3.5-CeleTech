package calebzhou.rdi.core.server.command.impl;

import calebzhou.rdi.core.server.RdiMemoryStorage;
import calebzhou.rdi.core.server.command.RdiCommand;
import calebzhou.rdi.core.server.utils.PlayerUtils;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.stream.Collectors;

import static calebzhou.rdi.core.server.RdiMemoryStorage.*;
import static calebzhou.rdi.core.server.utils.PlayerUtils.*;

/**
 * Created by calebzhou on 2022-10-01,8:55.
 */
public class SpeakScopeCommand extends RdiCommand {

	public SpeakScopeCommand() {
		super("speak-scope","设定聊天范围，是岛内可见还是全服可见");
	}

	@Override
	public LiteralArgumentBuilder<CommandSourceStack> getExecution() {
		return baseArgBuilder.executes(this::exec);
	}

	private int exec(CommandContext<CommandSourceStack> context) {
		ServerPlayer player = context.getSource().getPlayer();
		String pid = player.getStringUUID();
		if(pidToSpeakPlayersMap.containsKey(pid) && pidToSpeakPlayersMap.get(pid).size()>0){
			if(pidToSpeakPlayersMap.remove(pid)!=null){
				sendChatMessage(player,RESPONSE_SUCCESS,"您的聊天范围已经更改为：全服务器");
			}else{
				sendChatMessage(player,RESPONSE_INFO,"聊天范围已经更改为：全服务器（不知道成没成功，你先试试）");
			}
			return 1;
		}
		if(!isInIsland(player)){
			sendChatMessage(player, RESPONSE_ERROR,"必须在自己的岛屿上才能使用此功能！");
			return 1;
		}
		List<ServerPlayer> playersInLevel = getPlayersInLevel(player);
		if(playersInLevel.size()==0){
			sendChatMessage(player, RESPONSE_ERROR,"您的岛屿上目前没有成员，需要等待成员上线以后才能使用此功能！");
			return 1;
		}
		ObjectOpenHashSet<String> playerSet = new ObjectOpenHashSet<>();
		playerSet.addAll(playersInLevel.stream().map(Player::getScoreboardName).toList());
		pidToSpeakPlayersMap.put(pid,playerSet);

		sendChatMessage(player,RESPONSE_SUCCESS,"成功将聊天范围设定为：岛屿内可见。您的聊天消息只有以下玩家可见："+ playerSet);
		playersInLevel.forEach(playerInLevel->{
			sendChatMessage(playerInLevel,RESPONSE_INFO,"岛屿成员%s将聊天范围设定为了 仅岛屿内可见 ，您可以通过/speak-scope指令来设置。".formatted(player.getScoreboardName()));
		});
		return 1;
	}
}
