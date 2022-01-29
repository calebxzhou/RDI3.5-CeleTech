package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.command.NoArgCommand;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.utils.TextUtils;
import net.minecraft.server.network.ServerPlayerEntity;

public class TrashCommand extends NoArgCommand {
    public TrashCommand(String name, int permissionLevel) {
        super(name, permissionLevel,false);
    }

    @Override
    protected void onExecute(ServerPlayerEntity player) {
        player.getMainHandStack().setCount(0);
        TextUtils.sendChatMessage(player,"成功将手上的物品扔进了垃圾桶！", MessageType.SUCCESS);
    }
}
