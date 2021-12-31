package calebzhou.rdimc.celestech.utils;

import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

public class WorldUtils {

    public static int getDayTime(World world) {
        return (int)(world.getTimeOfDay() % 24000L);
    }
    public static boolean isNearbyLava(World world, BlockPos blockPos){
        //范围
        final int range = 5;
        int maxX = blockPos.getX()+range;
        int maxY = blockPos.getY()+range;
        int maxZ = blockPos.getZ()+range;

        int minX = blockPos.getX()-range;
        int minY = blockPos.getY()-range;
        int minZ = blockPos.getZ()-range;

        for(int x = minX; x < maxX ; x++) for(int z=minZ ; z< maxZ ;z++) for(int y=minY;y<maxY;y++){
            if(world.getBlockState(new BlockPos(x,y,z)).getBlock() == Blocks.LAVA){
                return true;
            }
        }
        return false;

    }

    public static boolean canPositionSeeSun(World world, BlockPos pos){
        int dayTime = getDayTime(world);
        //阳光角度,面向北方从3点钟->12->9->6->3
        double sunDegree =  (dayTime / 24000f) * 360;
        return getSkyLight(world,pos)==15;
    }
    public static int getSkyLight(World world,BlockPos bpos){
        return world.getLightingProvider().get(LightType.SKY).getLightLevel(bpos);
    }
    public static boolean isInWater(World world,BlockPos blockPos){
        return world.getFluidState(blockPos).getFluid() == Fluids.WATER ||
                world.getFluidState(blockPos).getFluid() == Fluids.FLOWING_WATER;
    }
}
