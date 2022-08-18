package calebzhou.rdimc.celestech.command.impl.island;

import calebzhou.rdimc.celestech.command.RdiCommand;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.model.PlayerLocation;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import calebzhou.rdimc.celestech.utils.PlayerUtils;
import calebzhou.rdimc.celestech.utils.ThreadPool;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

import static calebzhou.rdimc.celestech.utils.TextUtils.sendChatMessage;

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
            sendChatMessage(player,MessageType.ERROR,"您只能在自己的岛屿上设置传送点.");
            return 0;
        }
        ThreadPool.newThread(()->{
            String response = HttpUtils.sendRequest("put","island/"+player.getStringUUID()+"/"+new PlayerLocation(player).toInt().getXyzComma());

            sendChatMessage(player,response);
        });
        return 1;
    }


}
