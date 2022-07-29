package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.command.BaseCommand;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.model.PlayerLocation;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import calebzhou.rdimc.celestech.utils.PlayerUtils;
import net.minecraft.server.level.ServerPlayer;

import static calebzhou.rdimc.celestech.utils.TextUtils.sendChatMessage;

public class LocateCommand extends BaseCommand {
    public LocateCommand(String name, int permissionLevel) {
        super(name, permissionLevel,true);
    }

    @Override
    protected void onExecute(ServerPlayer player,String arg) {

        if(PlayerUtils.getDimensionName(player).equals("minecraft:the_nether")){
            sendChatMessage(player,MessageType.ERROR,"您只能在自己的岛屿上设置传送点.");
            return;
        }
        String response = HttpUtils.sendRequest("put","island/"+player.getStringUUID(),"xyz="+new PlayerLocation(player).getXyzComma());

        sendChatMessage(player,response);
    }

}
