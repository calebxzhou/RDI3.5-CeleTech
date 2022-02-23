package calebzhou.rdimc.celestech.utils;

import calebzhou.rdimc.celestech.RDICeleTech;
import calebzhou.rdimc.celestech.command.ExperienceException;
import calebzhou.rdimc.celestech.constant.ColorConstants;
import calebzhou.rdimc.celestech.constant.WorldConstants;
import calebzhou.rdimc.celestech.model.CoordLocation;
import calebzhou.rdimc.celestech.model.PlayerLocation;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.UUID;

public class PlayerUtils {
    public static void checkExpLevel(PlayerEntity player,int level){
        if(player.experienceLevel<level)
            throw new ExperienceException("经验不足，需要"+level+"级经验~");
        else
            player.addExperienceLevels(-level);
    }
    //添加缓降效果
    public static void addSlowFallEffect(PlayerEntity player){
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING,40,1));
        TextUtils.sendActionMessage(player, ColorConstants.GOLD+"感觉身体轻飘飘的...");
    }
    //传送,通过CoordLocation
    public static void teleport(PlayerEntity player, CoordLocation location){
        teleportPlayer(player,WorldConstants.SPAWN_LOCA.getWorld(), location.getPosX(), location.getPosY(), location.getPosZ(), 0f,0f);
    }
    public static void teleport(PlayerEntity player, PlayerLocation location){
        teleportPlayer(player,location.getWorld(), location.getPosX(), location.getPosY(), location.getPosZ(), location.getYaw(),location.getPitch());
    }
    //传送1到2 玩家
    public static void teleportPlayer(PlayerEntity player1,PlayerEntity player2){
        teleportPlayer(player1, (ServerWorld) player2.getWorld(), player2.getX(),
                player2.getY(), player2.getZ(), player2.getYaw(), player2.getPitch());
    }
    public static String  getDimensionName(Entity player){
        return player.getWorld().getDimension().getEffects().toString();
    }
    //传送到指定位置
    public static void teleportPlayer(PlayerEntity player, ServerWorld world, double x, double y, double z, float yaw, float pitch){
        BlockPos blockPos = new BlockPos(x, y, z);
        float warpYaw = MathHelper.wrapDegrees(yaw);
        float warpPitch = MathHelper.wrapDegrees(pitch);
            ChunkPos chunkPos = new ChunkPos(blockPos);
            world.getChunkManager().addTicket(ChunkTicketType.POST_TELEPORT, chunkPos, 1, player.getId());
            player.stopRiding();
            if (player.isSleeping()) {
                player.wakeUp(true, true);
            }
            if (world == player.world) {
                ((ServerPlayerEntity)player).networkHandler.requestTeleport(x, y, z, warpYaw, warpPitch, EnumSet.noneOf(PlayerPositionLookS2CPacket.Flag.class));
            } else {
                ((ServerPlayerEntity)player).teleport(world, x, y, z, warpYaw, warpPitch);
            }
        player.setHeadYaw(warpYaw);
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
    public static void placeBlock(World world, CoordLocation location, BlockState blockState){
       world.setBlockState(new BlockPos(location.getPosiX(),location.getPosiY(),location.getPosiZ()), blockState);
    }
    //发送新手套装
    public static void givePlayerInitialKit(ServerPlayerEntity player){
        player.getInventory().insertStack(new ItemStack(Items.OAK_SAPLING,1));
    }
    public static BlockPos getPlayerLookingAtBlock(PlayerEntity player, boolean isFluid){
        BlockHitResult rays=(BlockHitResult) player.raycast(64.0D,0.0f,isFluid);
        return rays.getBlockPos();
    }
    //通过玩家名获取对象
    public static ServerPlayerEntity getPlayerByName(String name){
        return RDICeleTech.getServer().getPlayerManager().getPlayer(name);
    }
    public static ServerPlayerEntity getPlayerByUuid(String uuid){
        return getPlayerByUuid(UUID.fromString(uuid));
    }
    public static ServerPlayerEntity getPlayerByUuid(UUID uuid){
        return RDICeleTech.getServer().getPlayerManager().getPlayer(uuid);
    }
    public static void resetSpawnPoint(ServerPlayerEntity player){
        player.setSpawnPoint(player.getSpawnPointDimension(),new BlockPos(0,220,0),0,true,true);
    }
}
