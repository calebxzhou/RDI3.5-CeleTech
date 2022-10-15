package calebzhou.rdi.core.server;

import calebzhou.rdi.core.server.constant.FileConst;
import calebzhou.rdi.core.server.constant.NetworkPackets;
import calebzhou.rdi.core.server.utils.PlayerUtils;
import calebzhou.rdi.core.server.utils.ThreadPool;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SaplingBlock;
import org.apache.commons.io.FileUtils;
import org.quiltmc.qsl.networking.api.PacketSender;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class RdiNetworkReceiver {
    //跳舞树
    private void danceTreeGrow(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl listener, FriendlyByteBuf buf, PacketSender sender) {
        try {
            String coord = buf.readUtf();
            String[] split = coord.split(",");
            int x= Integer.parseInt(split[0]);
            int y= Integer.parseInt(split[1]);
            int z= Integer.parseInt(split[2]);
            BlockPos bpos = new BlockPos(x,y,z);
            ServerLevel world = player.getLevel();
            Block block = world.getBlockState(bpos).getBlock();
            if(block instanceof SaplingBlock saplingBlock){
                RdiCoreServer.getServer().execute(()->saplingBlock.performBonemeal(world,player.getRandom(),bpos,world.getBlockState(bpos)));
            }
        } catch (Exception e) {
            RdiCoreServer.LOGGER.error(e.getStackTrace());
        }
    }
    //快速繁殖
    private void animalSex(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl listener, FriendlyByteBuf buf, PacketSender sender) {
        String eid = buf.readUtf();
        Entity entity = player.getLevel().getEntity(UUID.fromString(eid));
        if(entity==null)
            return;
        if(entity instanceof AgeableMob amob){
            if(player.experienceLevel<10){
                PlayerUtils.sendChatMessage(player,PlayerUtils.RESPONSE_ERROR,"精验不足，需要10");
                return;
            }
            player.experienceLevel-=10;
            amob.setAge(0);
            PlayerUtils.sendChatMessage(player,PlayerUtils.RESPONSE_SUCCESS,"生长成功");
        }
    }
    //硬件信息
    private void receiveHardwareSpec(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl listener, FriendlyByteBuf buf, PacketSender sender) {
        String specJson = buf.readUtf();
        ThreadPool.newThread(()->{
            try {
                File hwSpecFile = new File(FileConst.getHwSpecFolder(),player.getStringUUID()+".txt");
                if(!hwSpecFile.exists()) {
                    hwSpecFile.createNewFile();

                }
                //写入硬件信息
                FileUtils.write(hwSpecFile,specJson, StandardCharsets.UTF_8);
            } catch (IOException e) {
                RdiCoreServer.LOGGER.error(e.getStackTrace());
            }
        });
    }
}
