package calebzhou.rdimc.celestech.module;

import calebzhou.rdimc.celestech.api.NetworkReceivableC2S;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.model.PlayerLocation;
import calebzhou.rdimc.celestech.utils.PlayerUtils;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import static calebzhou.rdimc.celestech.RDICeleTech.MODID;
import static calebzhou.rdimc.celestech.utils.TextUtils.sendChatMessage;

public class Leap implements NetworkReceivableC2S {
    private static final Identifier LEAP_NETWORK=new Identifier(MODID,"leap");
    @Override
    public void registerNetworking() {
        ServerPlayNetworking.registerGlobalReceiver(LEAP_NETWORK,((server, player, handler, buf, responseSender) -> {
            String s = buf.readString();
            BlockPos bpos = BlockPos.fromLong(Long.parseLong(s));
            teleport(bpos,player);
        }));
    }
    private void teleport(BlockPos lookingAtBlock, ServerPlayerEntity fromPlayer){
        double distance = lookingAtBlock.getSquaredDistance(new Vec3i(fromPlayer.getX(), fromPlayer.getY(), fromPlayer.getZ()));
        PlayerUtils.checkExpLevel(fromPlayer, (int) Math.cbrt(distance)/2);
        PlayerUtils.teleport(fromPlayer, PlayerLocation.fromBlockPos(lookingAtBlock.add(0,2,0),fromPlayer.getWorld(), fromPlayer.getYaw(), fromPlayer.getPitch()));
        sendChatMessage(fromPlayer,"传送成功！", MessageType.SUCCESS);
    }
}
