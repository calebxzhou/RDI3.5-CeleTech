package calebzhou.rdimc.celestech.command.impl;

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

public class KickCommand implements RdiCommand {
    @Override
    public String getName() {
        return "kickout";
    }
    public LiteralArgumentBuilder<CommandSourceStack> getExecution() {
        return Commands.literal(getName()).then(Commands.argument("要踢出的玩家", EntityArgument.player())
                .executes(context -> exec(context.getSource().getPlayer(), EntityArgument.getPlayer(context, "要踢出的玩家"))));
    }
    private int exec(ServerPlayer fromPlayer, ServerPlayer kickPlayer) {
        if(fromPlayer==kickPlayer){
            TextUtils.sendChatMessage(fromPlayer, MessageType.ERROR,"您不可以踢出自己!!");
            return 0;
        }
        ThreadPool.newThread(() -> {
            String response = HttpUtils.sendRequest("delete", "island/crew/" + fromPlayer.getStringUUID()+"/"+kickPlayer.getStringUUID());
            if(response.equals("1")){
                TextUtils.sendChatMessage(kickPlayer,fromPlayer.getScoreboardName()+"删除了他的岛屿!");
                sendChatMessage(fromPlayer,MessageType.SUCCESS,"1");
            }

        });
        return 1;
    }


}
