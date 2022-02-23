package calebzhou.rdimc.celestech.module.protect;

import calebzhou.rdimc.celestech.command.BaseCommand;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.model.AreaSelection;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import calebzhou.rdimc.celestech.utils.PlayerUtils;
import calebzhou.rdimc.celestech.utils.TextUtils;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3i;

public class ProtectSetCommand extends BaseCommand {
    public ProtectSetCommand(String name, int permissionLevel) {
        super(name, permissionLevel,true);
    }


    @Override
    public void onExecute(ServerPlayerEntity player, String arg) {
        Vec3i[] area = AreaSelection.getPlayerSelectedArea(player.getUuidAsString());
        int distance = area[0].getManhattanDistance(area[1]);
        PlayerUtils.checkExpLevel(player,distance);


    }
}
