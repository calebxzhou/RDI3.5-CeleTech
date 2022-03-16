package calebzhou.rdimc.celestech.module.island.command;

import calebzhou.rdimc.celestech.command.BaseCommand;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.constant.WorldConstants;
import calebzhou.rdimc.celestech.model.ApiResponse;
import calebzhou.rdimc.celestech.model.CoordLocation;
import calebzhou.rdimc.celestech.model.Island;
import calebzhou.rdimc.celestech.utils.*;
import net.minecraft.server.level.ServerPlayer;

import static calebzhou.rdimc.celestech.utils.TextUtils.sendChatMessage;

public class DeleteCommand extends BaseCommand {
    public DeleteCommand(String name, int permissionLevel) {
        super(name, permissionLevel,true);
    }
    protected void onExecute(ServerPlayer player,String arg) {
        //TextUtils.sendChatMessage(player,"/confirm-delete");
        NetworkUtils.sendPacketS2C(player,NetworkUtils.ISLAND_INFO,"confirm-delete");
    }

}
