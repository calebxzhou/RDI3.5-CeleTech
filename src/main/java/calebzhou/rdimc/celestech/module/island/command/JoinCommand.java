package calebzhou.rdimc.celestech.module.island.command;

import calebzhou.rdimc.celestech.command.BaseCommand;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.model.ApiResponse;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import calebzhou.rdimc.celestech.utils.TextUtils;
import net.minecraft.server.network.ServerPlayerEntity;

public class JoinCommand extends BaseCommand {
    public JoinCommand(String command, int permissionLevel) {
        super(command, permissionLevel, true);
    }

    @Override
    protected void onExecute(ServerPlayerEntity player, String arg) {
        if(arg.equalsIgnoreCase("help")){
            TextUtils.sendChatMessage(player,"要加入朋友的空岛，使用指令/join 岛屿ID", MessageType.INFO);
            TextUtils.sendChatMessage(player,"例如 /join 12312312", MessageType.INFO);
            return;
        }
        ApiResponse response = HttpUtils.sendRequestV2("PUT", "v2/island/" + arg, "member=" + player.getUuidAsString(),"iid="+arg);
        TextUtils.sendChatMessage(player,"您成功加入了空岛！");
        TextUtils.sendChatMessage(player,response);
    }
}
