package calebzhou.rdimc.celestech.module;

import calebzhou.rdimc.celestech.api.NetworkReceivableC2S;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.model.PlayerLocation;
import calebzhou.rdimc.celestech.utils.PlayerUtils;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import static calebzhou.rdimc.celestech.RDICeleTech.MODID;
import static calebzhou.rdimc.celestech.utils.TextUtils.sendChatMessage;

public class Leap implements NetworkReceivableC2S {
    private static final ResourceLocation LEAP_NETWORK=new ResourceLocation(MODID,"leap");
    @Override
    public void registerNetworking() {
        ServerPlayNetworking.registerGlobalReceiver(LEAP_NETWORK,((server, player, handler, buf, responseSender) -> {
            String s = buf.readUtf();
            BlockPos bpos = BlockPos.of(Long.parseLong(s));
            teleport(bpos,player);
        }));
    }
    private void teleport(BlockPos lookingAtBlock, ServerPlayer fromPlayer){
        double distance = lookingAtBlock.distSqr(new Vec3i(fromPlayer.getX(), fromPlayer.getY(), fromPlayer.getZ()));
        int levelNeed = (int) Math.cbrt(distance) / 2;
        if(fromPlayer.experienceLevel<levelNeed) {
               sendChatMessage(fromPlayer,"经验不足，需要"+levelNeed+"经验！");
               return;
        }
        PlayerUtils.teleport(fromPlayer, PlayerLocation.fromBlockPos(lookingAtBlock.offset(0,2,0),fromPlayer.getLevel(), fromPlayer.getYRot(), fromPlayer.getXRot()));
        sendChatMessage(fromPlayer,"传送成功！", MessageType.SUCCESS);
    }
}
