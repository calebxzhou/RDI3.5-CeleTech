package calebzhou.rdi.core.server.command.impl;

import calebzhou.rdi.core.server.RdiMemoryStorage;
import calebzhou.rdi.core.server.command.RdiCommand;
import calebzhou.rdi.core.server.constant.ColorConst;
import calebzhou.rdi.core.server.utils.PlayerUtils;
import calebzhou.rdi.core.server.utils.ThreadPool;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;

import java.util.Timer;
import java.util.TimerTask;

import static calebzhou.rdi.core.server.utils.PlayerUtils.*;
import static calebzhou.rdi.core.server.utils.PlayerUtils.sendChatMessage;

public class TpaCommand extends RdiCommand {
	public TpaCommand() {
        super("tpa","传送到一个玩家身边");
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getExecution() {
        return baseArgBuilder.then(Commands.argument("玩家", EntityArgument.player())
                .executes(context -> exec(context.getSource().getPlayer(), EntityArgument.getPlayer(context, "玩家"))));
    }

    private int exec(ServerPlayer fromPlayer, ServerPlayer toPlayer) {
        String fromPlayerId = fromPlayer.getStringUUID();
        if(toPlayer==null){
            sendChatMessage(fromPlayer, RESPONSE_ERROR,"对方不在线！");
            return 1;
        }
        String toPlayerId = toPlayer.getStringUUID();
        /*if(fromPlayerId.equals(toPlayerId)){
            sendChatMessage(fromPlayer, RESPONSE_ERROR,"禁止原地TP");
            return 1;
        }*/
        if(fromPlayer.experienceLevel<3){
            sendChatMessage(fromPlayer, RESPONSE_ERROR,"经验不足,您需要3级经验.");
            return 1;
        }
        if(RdiMemoryStorage.tpaMap.containsKey(fromPlayer.getStringUUID())){
            sendChatMessage(fromPlayer, RESPONSE_ERROR,"您已经发送过传送请求了");
            return 1;
        }
        RdiMemoryStorage.tpaMap.put(toPlayerId,fromPlayerId);
        sendChatMessage(fromPlayer, RESPONSE_SUCCESS,"已经向%s发送了传送请求，15秒后传送请求将失效。".formatted(toPlayer.getScoreboardName()));
        fromPlayer.experienceLevel-=3;
        sendChatMessage(toPlayer, ColorConst.ORANGE+fromPlayer.getScoreboardName()+"想要传送到你的身边。");
        sendChatMessage(toPlayer, ColorConst.ORANGE+fromPlayer.getScoreboardName()+"要接受，输入/tpyes");
		ThreadPool.doAfter(15,()->RdiMemoryStorage.tpaMap.remove(toPlayerId));
        return 1;
    }



}
