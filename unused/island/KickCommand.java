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

public class KickCommand extends RdiCommand {
    @Override
    public String getName() {
        return "kickout";
    }
    public LiteralArgumentBuilder<CommandSourceStack> getExecution() {
        return baseArgBuilder.then(Commands.argument("要踢出的玩家", EntityArgument.player())
                .executes(context -> exec(context.getSource().getPlayer(), EntityArgument.getPlayer(context, "要踢出的玩家"))));
    }
    private int exec(ServerPlayer fromPlayer, ServerPlayer kickPlayer) {
        if(fromPlayer==kickPlayer){
            PlayerUtils.sendChatMessage(fromPlayer, PlayerUtils.RESPONSE_ERROR,"您不可以踢出自己!!");
            return 0;
        }
        ThreadPool.newThread(() -> {
            String response = HttpUtils.sendRequest("delete", "island/crew/" + fromPlayer.getStringUUID()+"/"+kickPlayer.getStringUUID());
            if(response.equals("1")){
                PlayerUtils.sendChatMessage(kickPlayer,fromPlayer.getScoreboardName()+"删除了他的岛屿!");
                sendChatMessage(fromPlayer,PlayerUtils.RESPONSE_SUCCESS,"1");
            }

        });
        return 1;
    }


}
