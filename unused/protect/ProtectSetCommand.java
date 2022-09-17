package calebzhou.rdi.celestech.module.protect;

import calebzhou.rdi.celestech.command.BaseCommand;
import calebzhou.rdi.celestech.model.AreaSelection;
import calebzhou.rdi.celestech.utils.PlayerUtils;
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
