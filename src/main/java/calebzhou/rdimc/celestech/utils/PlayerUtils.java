package calebzhou.rdimc.celestech.utils;

import calebzhou.rdimc.celestech.model.PlayerLocation;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PlayerUtils {
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
    public static void randomTeleport(ServerPlayerEntity player){
        int x=RandomUtils.generateRandomInt(-9999,9999);
        int z=RandomUtils.generateRandomInt(-9999,9999);
        PlayerUtils.placeBlock(player.getWorld(),x,220,z,"minecraft:obsidian");
        teleportPlayer(player,"minecraft:overworld",x+0.5,221,z+0.5,0,0);
    }
    public static void placeBlock(World world, int x, int y, int z, String blockId){
       world.setBlockState(new BlockPos(x,y,z), Blocks.OBSIDIAN.getDefaultState());
    }
}
