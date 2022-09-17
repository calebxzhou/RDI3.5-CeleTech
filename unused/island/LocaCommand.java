package calebzhou.rdi.celestech.command.impl.island;

import calebzhou.rdi.celestech.command.RdiCommand;
import calebzhou.rdi.celestech.constant.MessageType;
import calebzhou.rdi.celestech.model.PlayerLocation;
import calebzhou.rdi.celestech.utils.HttpUtils;
import calebzhou.rdi.celestech.utils.PlayerUtils;
import calebzhou.rdi.celestech.utils.ThreadPool;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

import static calebzhou.rdi.celestech.utils.TextUtils.sendChatMessage;

public class LocaCommand extends RdiCommand {
    @Override
    public String getName() {
        return "loca";
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getExecution() {
        return baseArgBuilder.executes(context -> exec(context.getSource().getPlayer()));
    }
    private int exec(ServerPlayer player) {
        if(PlayerUtils.getDimensionName(player).equals("minecraft:the_nether")){
            sendChatMessage(player,PlayerUtils.RESPONSE_ERROR,"您只能在自己的岛屿上设置传送点.");
            return 0;
        }
        ThreadPool.newThread(()->{
            String response = HttpUtils.sendRequest("put","island/"+player.getStringUUID()+"/"+new PlayerLocation(player).toInt().getXyzComma());

            sendChatMessage(player,response);
        });
        return 1;
    }


}
