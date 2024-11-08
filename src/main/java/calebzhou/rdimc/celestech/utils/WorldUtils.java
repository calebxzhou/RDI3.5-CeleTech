package calebzhou.rdimc.celestech.utils;

import calebzhou.rdimc.celestech.RDICeleTech;
import calebzhou.rdimc.celestech.RdiSharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
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
    public static ServerLevel getNether(){
        return RDICeleTech.getServer().getLevel(ServerLevel.NETHER);
    }

    public static Vec3i getIsland2ToNetherPos(int islandId){
        int netherRatioX=40;
        int netherRatioZ=40;
        //0=一象限 3=四象限
        int quadrant = islandId % 4;
        switch (quadrant){
            case 1-> {
                netherRatioX *= -1;
            }
            case 2-> {
                netherRatioX *= -1;
                netherRatioZ *= -1;
            }
            case 3-> {
                netherRatioZ *= -1;
            }
        }

        int netherTargetX = islandId * netherRatioX;
        int netherTargetZ = islandId * netherRatioZ;
        final int netherTargetY = 96;

        return new BlockPos(netherTargetX,netherTargetY,netherTargetZ);
    }
    public static int getIsland2IdInt(Level level){
        return Integer.parseInt(getIsland2Id(level));
    }
    public static String getIsland2Id(Level level){
        return getDimensionName(level).replace(RdiSharedConstants.ISLAND_DIMENSION_FULL_PREFIX, "");
    }
    public static boolean isInIsland2(Level level){
        return WorldUtils.getDimensionName(level)
                .startsWith(RdiSharedConstants.ISLAND_DIMENSION_FULL_PREFIX);
    }
    public static String getDimensionName( Level level){
        return level.dimension().location().toString();
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
    public static void placeBlock(Level world, BlockPos bpos, BlockState blockState){
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
