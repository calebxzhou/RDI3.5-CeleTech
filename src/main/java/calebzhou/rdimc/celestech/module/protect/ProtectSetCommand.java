package calebzhou.rdimc.celestech.module.protect;

import calebzhou.rdimc.celestech.command.BaseCommand;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.model.AreaSelection;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import calebzhou.rdimc.celestech.utils.PlayerUtils;
import calebzhou.rdimc.celestech.utils.TextUtils;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerPlayer;

public class ProtectSetCommand extends BaseCommand {
    public ProtectSetCommand(String name, int permissionLevel) {
        super(name, permissionLevel,true);
    }


    @Override
    public void onExecute(ServerPlayer player, String arg) {
        Vec3i[] area = AreaSelection.getPlayerSelectedArea(player.getStringUUID());
        int distance = area[0].distManhattan(area[1]);
        PlayerUtils.checkExpLevel(player,distance);



    }
}
