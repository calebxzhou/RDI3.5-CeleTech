package calebzhou.rdimc.celestech.module.protect;

import calebzhou.rdimc.celestech.command.AreaArgCommand;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import calebzhou.rdimc.celestech.utils.PlayerUtils;
import calebzhou.rdimc.celestech.utils.TextUtils;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3i;

public class SetProtectCommand extends AreaArgCommand {
    public SetProtectCommand(String name, int permissionLevel) {
        super(name, permissionLevel,true);
    }


    @Override
    public void onExecute(ServerPlayerEntity player, String arg) {
        Vec3i[] pos2 = parseToPosition(arg);
        double dist = Math.sqrt(pos2[0].getSquaredDistance(pos2[1]))*0.5;
        if(!PlayerUtils.checkExpLevel(player,(int)dist)){
            TextUtils.sendChatMessage(player,"将您指定的区域设置成保护区，需要"+(int)dist+"级经验。", MessageType.ERROR);
            return;
        }
        HttpUtils.sendRequest("POST","protect/"+arg,"pid="+player.getUuidAsString());


    }
}
