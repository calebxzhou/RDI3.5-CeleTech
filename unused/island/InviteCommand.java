package calebzhou.rdi.celestech.command.impl.island;

import calebzhou.rdi.celestech.command.RdiCommand;
import calebzhou.rdi.celestech.constant.MessageType;
import calebzhou.rdi.celestech.utils.HttpUtils;
import calebzhou.rdi.celestech.utils.TextUtils;
import calebzhou.rdi.celestech.utils.ThreadPool;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;

import static calebzhou.rdi.celestech.utils.TextUtils.sendChatMessage;

public class InviteCommand extends RdiCommand {

    @Override
    public String getName() {
        return "invite";
    }

    public LiteralArgumentBuilder<CommandSourceStack> getExecution() {
        return baseArgBuilder.then(Commands.argument("要邀请的玩家", EntityArgument.player())
                .executes(context -> exec(context.getSource().getPlayer(), EntityArgument.getPlayer(context, "要邀请的玩家"))));
    }

    private int exec(ServerPlayer player, ServerPlayer invitedPlayer) {
        String invitedPlayerName = invitedPlayer.getScoreboardName();
        if(player.getScoreboardName().equalsIgnoreCase(invitedPlayerName)){
            PlayerUtils.sendChatMessage(player,PlayerUtils.RESPONSE_ERROR,"您不可以邀请自己加入岛屿!");
            return 0;
        }
        ThreadPool.newThread(() -> {
            String response = HttpUtils.sendRequest("post", "island/crew/" + player.getStringUUID()+"/"+invitedPlayer.getStringUUID());
            PlayerUtils.sendChatMessage(player,"您邀请了"+invitedPlayer);
            if(response.equals("1")){
                PlayerUtils.sendChatMessage(invitedPlayer,PlayerUtils.RESPONSE_INFO,player.getScoreboardName()+"邀请您加入了他的岛屿");
                TextUtils.sendClickableContent(invitedPlayer,"按下[H键]可以前往他的岛屿","/home");
                sendChatMessage(player,PlayerUtils.RESPONSE_SUCCESS,"1");
            }
        });

        return 1;
    }


}
