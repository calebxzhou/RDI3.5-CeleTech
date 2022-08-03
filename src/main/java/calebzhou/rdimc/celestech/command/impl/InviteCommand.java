package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.command.BaseCommand;
import calebzhou.rdimc.celestech.command.RdiCommand;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import calebzhou.rdimc.celestech.utils.TextUtils;
import calebzhou.rdimc.celestech.utils.ThreadPool;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;

import static calebzhou.rdimc.celestech.utils.TextUtils.sendChatMessage;

public class InviteCommand implements RdiCommand {

    @Override
    public String getName() {
        return "invite";
    }

    public LiteralArgumentBuilder<CommandSourceStack> getExecution() {
        return Commands.literal(getName()).then(Commands.argument("要邀请的玩家", EntityArgument.player())
                .executes(context -> exec(context.getSource().getPlayer(), EntityArgument.getPlayer(context, "要邀请的玩家"))));
    }

    private int exec(ServerPlayer player, ServerPlayer invitedPlayer) {
        String invitedPlayerName = invitedPlayer.getScoreboardName();
        if(player.getScoreboardName().equalsIgnoreCase(invitedPlayerName)){
            TextUtils.sendChatMessage(player,MessageType.ERROR,"您不可以邀请自己加入岛屿!");
            return 0;
        }
        ThreadPool.newThread(() -> {
            String response = HttpUtils.sendRequest("post", "island/crew/" + player.getStringUUID()+"/"+invitedPlayer.getStringUUID());
            TextUtils.sendChatMessage(player,"您邀请了"+invitedPlayer);
            if(response.equals("1")){
                TextUtils.sendChatMessage(invitedPlayer,MessageType.INFO,player.getScoreboardName()+"邀请您加入了他的岛屿");
                TextUtils.sendClickableContent(invitedPlayer,"按下[H键]可以前往他的岛屿","/home");
                sendChatMessage(player,MessageType.SUCCESS,"1");
            }
        });

        return 1;
    }


}
