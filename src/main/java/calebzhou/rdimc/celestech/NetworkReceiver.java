package calebzhou.rdimc.celestech;

import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.model.PlayerLocation;
import calebzhou.rdimc.celestech.utils.PlayerUtils;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SaplingBlock;

import static calebzhou.rdimc.celestech.utils.TextUtils.sendChatMessage;

public class NetworkReceiver {
    public NetworkReceiver(){
        //挂机检测
        ServerPlayNetworking.registerGlobalReceiver(NetworkPackets.AFK_DETECT,((server, player, handler, buf, responseSender) -> {
            String[] s = buf.readUtf().split(",");
            String pname = s[0];
            String status = s[1];
            int tick = Integer.parseInt(s[2]);
            if(status.equals("afk"))
                RDICeleTech.afkMap.put(pname,tick);
            else
                RDICeleTech.afkMap.removeInt(pname);
        }));
        //跳舞树
        ServerPlayNetworking.registerGlobalReceiver(NetworkPackets.DANCE_TREE_GROW,((server, player, handler, buf, responseSender) -> {
            String s = buf.readUtf();
            BlockPos bpos = BlockPos.of(Long.parseLong(s));
            ServerLevel world = player.getLevel();
            Block block = world.getBlockState(bpos).getBlock();
            if(block instanceof SaplingBlock saplingBlock){
                saplingBlock.performBonemeal(world,player.getRandom(),bpos,world.getBlockState(bpos));
            }
        }));
        //隔空跳跃
        ServerPlayNetworking.registerGlobalReceiver(NetworkPackets.LEAP,((server, player, handler, buf, responseSender) -> {
            String s = buf.readUtf();
            BlockPos bpos = BlockPos.of(Long.parseLong(s));
            double distance = bpos.distSqr(new Vec3i(player.getX(), player.getY(), player.getZ()));
            int levelNeed = (int) Math.cbrt(distance) / 2;
            if(player.experienceLevel<levelNeed) {
                sendChatMessage(player,"经验不足，需要"+levelNeed+"经验！");
                return;
            }
            PlayerUtils.teleport(player, PlayerLocation.fromBlockPos(bpos.offset(0,2,0),player.getLevel(), player.getYRot(), player.getXRot()));
            sendChatMessage(player,"传送成功！", MessageType.SUCCESS);
        }));

    }
}
