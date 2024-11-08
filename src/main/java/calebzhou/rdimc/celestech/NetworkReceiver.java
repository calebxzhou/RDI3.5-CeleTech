package calebzhou.rdimc.celestech;

import calebzhou.rdimc.celestech.constant.FileConst;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.utils.TextUtils;
import calebzhou.rdimc.celestech.utils.ThreadPool;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
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

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class NetworkReceiver {
    public static final NetworkReceiver INSTANCE = new NetworkReceiver();
    private NetworkReceiver(){ }
    public void register(){
        ServerPlayNetworking.registerGlobalReceiver(NetworkPackets.AFK_DETECT,this::afkDetect);
        ServerPlayNetworking.registerGlobalReceiver(NetworkPackets.DANCE_TREE_GROW,this::danceTreeGrow);
        ServerPlayNetworking.registerGlobalReceiver(NetworkPackets.SAVE_WORLD,this::saveWorld);
        ServerPlayNetworking.registerGlobalReceiver(NetworkPackets.ANIMAL_SEX,this::animalSex);
        ServerPlayNetworking.registerGlobalReceiver(NetworkPackets.HW_SPEC,this::receiveHardwareSpec );
    }
    //挂机检测
    private void afkDetect(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl listener, FriendlyByteBuf buf, PacketSender sender) {
        try {
            int afkTicks = buf.readInt();
            if(afkTicks>0)
                RdiMemoryStorage.afkMap.put(player.getScoreboardName(), afkTicks);
            else
                RdiMemoryStorage.afkMap.removeInt(player.getScoreboardName());
        } catch (Exception e) {
                RDICeleTech.LOGGER.error(e.getStackTrace());
        }
    }
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
                RDICeleTech.getServer().execute(()->saplingBlock.performBonemeal(world,player.getRandom(),bpos,world.getBlockState(bpos)));
            }
        } catch (Exception e) {
            RDICeleTech.LOGGER.error(e.getStackTrace());
        }
    }
    //保存地图
    private void saveWorld(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl listener, FriendlyByteBuf buf, PacketSender sender) {
        RDICeleTech.getServer().saveEverything(true, true, true);
    }
    //快速繁殖
    private void animalSex(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl listener, FriendlyByteBuf buf, PacketSender sender) {
        String eid = buf.readUtf();
        Entity entity = player.getLevel().getEntity(UUID.fromString(eid));
        if(entity==null)
            return;
        if(entity instanceof AgeableMob amob){
            if(player.experienceLevel<10){
                TextUtils.sendChatMessage(player,MessageType.ERROR,"精验不足，需要10");
                return;
            }
            player.experienceLevel-=10;
            amob.setAge(0);
            TextUtils.sendChatMessage(player,MessageType.SUCCESS,"生长成功");
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
                RDICeleTech.LOGGER.error(e.getStackTrace());
            }
        });
    }
}
//隔空跳跃
        /*ServerPlayNetworking.registerGlobalReceiver(NetworkPackets.LEAP,((server, player, handler, buf, responseSender) -> {
            String s = buf.readUtf();
            BlockPos bpos = BlockPos.of(Long.parseLong(s));
            double distance = bpos.distSqr(new Vec3i(player.getX(), player.getY(), player.getZ()));
            int levelNeed = (int) Math.cbrt(distance) / 2;
            if(player.experienceLevel<levelNeed) {
                sendChatMessage(player,"经验不足，需要"+levelNeed+"经验！");
                return;
            }
            PlayerUtils.teleport(player, new PlayerLocation(bpos.offset(0,2,0),player.getLevel(), player.getYRot(), player.getXRot()));
            sendChatMessage(player, MessageType.SUCCESS,"1");
        }));*/