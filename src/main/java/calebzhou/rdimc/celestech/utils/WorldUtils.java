package calebzhou.rdimc.celestech.utils;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.PalettedContainer;

public class WorldUtils {
    public static void changeBiome(BlockPos bpos,ServerWorld world,Biome biome){
        Chunk chunk = world.getChunk(bpos.getX()>>4,bpos.getZ()>>4);
        int sectionYindex = (bpos.getY()+64)>>4;
        PalettedContainer<Biome> biomeArray = chunk.getSection(sectionYindex).getBiomeContainer();
        int mx=bpos.getX()&3;
        int my=bpos.getY()&3;
        int mz=bpos.getZ()&3;
        biomeArray.swap(mx,my,mz, (biome));
        chunk.setShouldSave(true);
    }
    public static void fill(ServerWorld serverWorld, BlockBox range, BlockState block){
        for (BlockPos blockPos : BlockPos.iterate(range.getMinX(), range.getMinY(), range.getMinZ(), range.getMaxX(), range.getMaxY(), range.getMaxZ())) {
            serverWorld.setBlockState(blockPos, block);
        }
    }
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
    public static PlayerEntity getNearestPlayer(WorldAccess world, BlockPos pos){
        return  world.getClosestPlayer(pos.getX(), pos.getY(), pos.getZ(), 20,false);
    }
}
