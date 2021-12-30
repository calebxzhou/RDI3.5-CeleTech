package calebzhou.rdimc.celestech.utils;

import calebzhou.rdimc.celestech.RDICeleTech;
import calebzhou.rdimc.celestech.enums.ColorConst;
import calebzhou.rdimc.celestech.model.CoordLocation;
import calebzhou.rdimc.celestech.model.PlayerLocation;
import calebzhou.rdimc.celestech.model.thread.LoadingBarThread;
import net.minecraft.block.Blocks;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.UUID;

public class PlayerUtils {
    public static void addSlowFallEffect(PlayerEntity player){
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING,40,1));
        TextUtils.sendActionMessage(player, ColorConst.GOLD+"感觉身体轻飘飘的...");
    }
    public static void teleport(PlayerEntity player, CoordLocation location){
        teleportPlayer(player,"minecraft:overworld", location.getPosX(), location.getPosY(), location.getPosZ(), 0f,0f);
    }
    public static void teleportPlayer(PlayerEntity player1,PlayerEntity player2){
        teleportPlayer(player1,player2.getWorld().getDimension().getEffects().toString(), player2.getX(),
                player2.getY(), player2.getZ(), player2.getYaw(), player2.getPitch());
    }
    public static void teleportPlayer(PlayerEntity player, String world, double x, double y, double z, float w, float p){
        String cmd="execute as %player in %world rotated %yaw %pitch run tp %x %y %z"
                        .replace("%player",player.getDisplayName().getString())
                        .replace("%world", world)
                        .replace("%yaw", String.valueOf(w))
                        .replace("%pitch", String.valueOf(p))
                        .replace("%x", String.valueOf(x))
                        .replace("%y", String.valueOf(y))
                        .replace("%z", String.valueOf(z));
        ServerUtils.executeCommandOnServer(cmd);
    }
    public static void placeBlock(World world, CoordLocation location, String blockId){
       world.setBlockState(new BlockPos(location.getPosiX(),location.getPosiY(),location.getPosiZ()), Blocks.OBSIDIAN.getDefaultState());
    }
    public static void sendLoading(ServerPlayerEntity player){
        ThreadPool.newThread(new LoadingBarThread(player,1500));
    }
    public static void givePlayerInitialKit(ServerPlayerEntity player){
        player.getInventory().insertStack(new ItemStack(Items.ACACIA_SAPLING,1));
        player.getInventory().insertStack(new ItemStack(Items.WATER_BUCKET,1));
        player.getInventory().insertStack(new ItemStack(Items.LAVA_BUCKET,1));
        player.getInventory().insertStack(new ItemStack(Items.GRASS_BLOCK,8));
        player.getInventory().insertStack(new ItemStack(Items.DIRT,16));
        player.getInventory().insertStack(new ItemStack(Items.CHEST,1));
    }
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
