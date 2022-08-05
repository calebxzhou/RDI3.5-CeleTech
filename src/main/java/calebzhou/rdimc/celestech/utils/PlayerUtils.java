package calebzhou.rdimc.celestech.utils;

import calebzhou.rdimc.celestech.RDICeleTech;
import calebzhou.rdimc.celestech.constant.FileConst;
import calebzhou.rdimc.celestech.model.PlayerLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.TicketType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.io.File;
import java.util.EnumSet;
import java.util.UUID;

public class PlayerUtils {
   /* public static void checkExpLevel(Player player,int level){
        if(player.experienceLevel<level)
            throw new ExperienceException("经验不足，需要"+level+"级经验~");
        else
            player.giveExperienceLevels(-level);
    }*/
    public static File getPasswordFile(ServerPlayer player)
    {
        return getPasswordFile(player.getStringUUID());
    }
    public static File getPasswordFile(String playerUuid){
        return  new File(FileConst.PASSWORD_FOLDER,playerUuid+".txt");
    }
    public static void teleport(Player player, PlayerLocation location){
        teleportPlayer(player,location.world, location.x, location.y, location.z, location.w,location.p);
    }
    //传送1到2 玩家
    public static void teleportPlayer(Player player1,Player player2){
        teleportPlayer(player1, (ServerLevel) player2.getLevel(), player2.getX(),
                player2.getY(), player2.getZ(), player2.getYRot(), player2.getXRot());
    }
    public static String  getDimensionName(Entity player){
        return player.getLevel().dimensionType().effectsLocation().toString();
    }
    //传送到指定位置
    public static void teleportPlayer(Player player, ServerLevel world, double x, double y, double z, float yaw, float pitch){
        if(world==null)
            world=RDICeleTech.getServer().overworld();
        BlockPos blockPos = new BlockPos(x, y, z);
        float warpYaw = Mth.wrapDegrees(yaw);
        float warpPitch = Mth.wrapDegrees(pitch);
            ChunkPos chunkPos = new ChunkPos(blockPos);
            world.getChunkSource().addRegionTicket(TicketType.POST_TELEPORT, chunkPos, 1, player.getId());
            player.stopRiding();
            if (player.isSleeping()) {
                player.stopSleepInBed(true, true);
            }
            if (world == player.level) {
                ((ServerPlayer)player).connection.teleport(x, y, z, warpYaw, warpPitch, EnumSet.noneOf(ClientboundPlayerPositionPacket.RelativeArgument.class));
            } else {
                ((ServerPlayer)player).teleportTo(world, x, y, z, warpYaw, warpPitch);
            }
        player.setYHeadRot(warpYaw);
        /*String cmd="execute as %player in %world rotated %yaw %pitch run tp %x %y %z"
                        .replace("%player",player.getDisplayName().getString())
                        .replace("%world", world)
                        .replace("%yaw", String.valueOf(w))
                        .replace("%pitch", String.valueOf(p))
                        .replace("%x", String.valueOf(x))
                        .replace("%y", String.valueOf(y))
                        .replace("%z", String.valueOf(z));
        ServerUtils.executeCommandOnServer(cmd);*/
    }
    //在世界放置方块
    public static void placeBlock(Level world, PlayerLocation location, BlockState blockState){
       world.setBlockAndUpdate(new BlockPos(location.x,location.y,location.z), blockState);
    }
    //发送新手套装
    public static void givePlayerInitialKit(ServerPlayer player){
        player.getInventory().add(new ItemStack(Items.OAK_SAPLING,1));
    }
    public static BlockPos getPlayerLookingAtBlock(Player player, boolean isFluid){
        BlockHitResult rays=(BlockHitResult) player.pick(64.0D,0.0f,isFluid);
        return rays.getBlockPos();
    }
    //通过玩家名获取对象
    public static ServerPlayer getPlayerByName(String name){
        return RDICeleTech.getServer().getPlayerList().getPlayerByName(name);
    }
    public static ServerPlayer getPlayerByUuid(String uuid){
        return getPlayerByUuid(UUID.fromString(uuid));
    }
    public static ServerPlayer getPlayerByUuid(UUID uuid){
        return RDICeleTech.getServer().getPlayerList().getPlayer(uuid);
    }
    public static void resetSpawnPoint(ServerPlayer player){
        player.setRespawnPosition(player.getRespawnDimension(),new BlockPos(0,220,0),0,true,true);
    }
}
