package calebzhou.rdimc.celestech.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.material.Fluids;

public class WorldUtils {
    public static final BlockPos INIT_POS = new BlockPos(0,127,0);
    public static void fill(ServerLevel serverWorld, BoundingBox range, BlockState block){
        for (BlockPos blockPos : BlockPos.betweenClosed(range.minX(), range.minY(), range.minZ(), range.maxX(), range.maxY(), range.maxZ())) {
            serverWorld.setBlockAndUpdate(blockPos, block);
        }
    }
    public static int getDayTime(Level world) {
        return (int)(world.getDayTime() % 24000L);
    }
    public static boolean isNearbyLava(Level world, BlockPos blockPos){
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
    public static void placeBlock(Level world,BlockPos bpos,BlockState blockState){
        world.setBlockAndUpdate(bpos,blockState);
    }
    public static void placeInitialBlocks(Level world){
        BlockPos basePos = new BlockPos(0,127,0);
        placeBlock(world,basePos.offset(0,0,0),Blocks.DIRT.defaultBlockState());
        placeBlock(world,basePos.offset(0,1,0),Blocks.OAK_SAPLING.defaultBlockState());
        placeBlock(world,basePos.offset(0,0,-1),Blocks.OBSIDIAN.defaultBlockState());
        placeBlock(world,basePos.offset(-1,0,0),Blocks.OBSIDIAN.defaultBlockState());
        placeBlock(world,basePos.offset(-1,0,-1),Blocks.OBSIDIAN.defaultBlockState());
    }
    public static boolean canPositionSeeSun(Level world, BlockPos pos){
        int dayTime = getDayTime(world);
        //阳光角度,面向北方从3点钟->12->9->6->3
        double sunDegree =  (dayTime / 24000f) * 360;
        return getSkyLight(world,pos)==15;
    }
    public static int getSkyLight(Level world,BlockPos bpos){
        return world.getLightEngine().getLayerListener(LightLayer.SKY).getLightValue(bpos);
    }
    public static boolean isInWater(Level world,BlockPos blockPos){
        return world.getFluidState(blockPos).getType() == Fluids.WATER ||
                world.getFluidState(blockPos).getType() == Fluids.FLOWING_WATER;
    }
    public static Player getNearestPlayer(LevelAccessor world, BlockPos pos){
        return  world.getNearestPlayer(pos.getX(), pos.getY(), pos.getZ(), 20,false);
    }
}
