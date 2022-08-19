package calebzhou.rdimc.celestech;

import calebzhou.rdimc.celestech.constant.FileConst;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.model.PlayerLocation;
import calebzhou.rdimc.celestech.utils.PlayerUtils;
import calebzhou.rdimc.celestech.utils.TextUtils;
import calebzhou.rdimc.celestech.utils.ThreadPool;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SaplingBlock;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static calebzhou.rdimc.celestech.utils.TextUtils.sendChatMessage;

public class NetworkReceiver {
    public NetworkReceiver(){
        //挂机检测
        ServerPlayNetworking.registerGlobalReceiver(NetworkPackets.AFK_DETECT,((server, player, handler, buf, responseSender) -> {
            try {
            int afkTicks = buf.readInt();
                if(afkTicks>0)
                    RDICeleTech.afkMap.put(player.getScoreboardName(), afkTicks);
                else
                    RDICeleTech.afkMap.removeInt(player.getScoreboardName());
            } catch (Exception e) {
                RDICeleTech.LOGGER.error(e.getStackTrace());
            }
        }));
        //跳舞树
        ServerPlayNetworking.registerGlobalReceiver(NetworkPackets.DANCE_TREE_GROW,((server, player, handler, buf, responseSender) -> {
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
                    saplingBlock.performBonemeal(world,player.getRandom(),bpos,world.getBlockState(bpos));
                }
            } catch (Exception e) {
                RDICeleTech.LOGGER.error(e.getStackTrace());
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
            PlayerUtils.teleport(player, new PlayerLocation(bpos.offset(0,2,0),player.getLevel(), player.getYRot(), player.getXRot()));
            sendChatMessage(player, MessageType.SUCCESS,"1");
        }));
        //保存地图
        ServerPlayNetworking.registerGlobalReceiver(NetworkPackets.SAVE_WORLD,((server, player, handler, buf, responseSender) -> {
            RDICeleTech.getServer().saveEverything(true, true, true);
        }));
        //快速繁殖
        ServerPlayNetworking.registerGlobalReceiver(NetworkPackets.ANIMAL_SEX,((server, player, handler, buf, responseSender) -> {
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




        }));
        //硬件信息
        ServerPlayNetworking.registerGlobalReceiver(NetworkPackets.HW_SPEC,((server, player, handler, buf, responseSender) -> {
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
        }));

    }
}
