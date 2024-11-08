package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.RdiMemoryStorage;
import calebzhou.rdimc.celestech.command.RdiCommand;
import calebzhou.rdimc.celestech.constant.ColorConst;
import calebzhou.rdimc.celestech.constant.MessageType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;

import java.util.Timer;
import java.util.TimerTask;

import static calebzhou.rdimc.celestech.utils.TextUtils.getClickableContentComp;
import static calebzhou.rdimc.celestech.utils.TextUtils.sendChatMessage;

public class TpaCommand extends RdiCommand {
    public TpaCommand() {
        super("tpa");
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getExecution() {
        return baseArgBuilder.then(Commands.argument("玩家", EntityArgument.player())
                .executes(context -> exec(context.getSource().getPlayer(), EntityArgument.getPlayer(context, "玩家"))));
    }

    private int exec(ServerPlayer fromPlayer, ServerPlayer toPlayer) {
        String fromPlayerId = fromPlayer.getStringUUID();
        if(toPlayer==null){
            sendChatMessage(fromPlayer,MessageType.ERROR,"对方不在线！");
            return 1;
        }
        String toPlayerId = toPlayer.getStringUUID();
        if(fromPlayerId.equals(toPlayerId)){
            sendChatMessage(fromPlayer,MessageType.ERROR,"禁止原地TP");
            return 1;
        }
        if(fromPlayer.experienceLevel<3){
            sendChatMessage(fromPlayer,MessageType.ERROR,"经验不足,您需要3级经验.");
            return 1;
        }
        if(RdiMemoryStorage.tpaMap.containsKey(fromPlayer.getStringUUID())){
            sendChatMessage(fromPlayer,MessageType.ERROR,"您已经发送过传送请求了");
            return 1;
        }
        RdiMemoryStorage.tpaMap.put(fromPlayerId,toPlayerId);
        sendChatMessage(fromPlayer,MessageType.SUCCESS,"已经向%s发送了传送请求，15秒后传送请求将失效。".formatted(fromPlayer.getScoreboardName()));
        fromPlayer.experienceLevel-=3;
        sendChatMessage(toPlayer, ColorConst.ORANGE+fromPlayer.getScoreboardName()+"想要传送到你的身边。");
        MutableComponent tpyes= getClickableContentComp(ColorConst.BRIGHT_GREEN+"[接受]"+ ColorConst.RESET,"/tpreq true_false_"+fromPlayerId," ");
        MutableComponent tpyes2= getClickableContentComp(ColorConst.AQUA+"[以仅参观模式接受]"+ ColorConst.RESET,"/tpreq true_true_"+fromPlayerId,"对方将没有破坏权限!");
        MutableComponent tpwait= getClickableContentComp(ColorConst.GOLD+"[等我一下]"+ ColorConst.RESET,"稍等"," ");
        MutableComponent tpdeny= getClickableContentComp(ColorConst.RED+"[拒绝]"+ ColorConst.RESET,"/tpreq false_false_"+fromPlayerId," ");
        sendChatMessage(toPlayer,tpyes.append(tpyes2).append(tpwait).append(tpdeny));
        Timer timer = new Timer();
        timer.schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        RdiMemoryStorage.tpaMap.remove(fromPlayerId);
                        this.cancel();
                    }
                },
                15*1000
        );
        return 1;
    }


 
}
