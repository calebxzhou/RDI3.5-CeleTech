package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.command.ArgCommand;
import calebzhou.rdimc.celestech.command.BaseCommand;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.model.PlayerLocation;
import calebzhou.rdimc.celestech.utils.PlayerUtils;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.math.Vector3d;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import static calebzhou.rdimc.celestech.utils.TextUtils.*;

public class LeapCommand extends BaseCommand implements ArgCommand {
    public LeapCommand(String name, int permissionLevel) {
        super(name, permissionLevel,false);
    }

    @Override
    public void onExecute(ServerPlayerEntity fromPlayer, String arg) {
        /*BlockPos lookingAtBlock = BlockPos.fromLong(Long.parseLong(arg));
        double distance = lookingAtBlock.getSquaredDistance(new Vec3i(fromPlayer.getX(), fromPlayer.getY(), fromPlayer.getZ()));
        PlayerUtils.checkExpLevel(fromPlayer, (int) Math.cbrt(distance)/2);
        PlayerUtils.teleport(fromPlayer, PlayerLocation.fromBlockPos(lookingAtBlock.add(0,2,0),fromPlayer.getWorld(), fromPlayer.getYaw(), fromPlayer.getPitch()));*/
        sendChatMessage(fromPlayer,"请升级新版客户端，才能使用本功能。",MessageType.ERROR);
    }
}
