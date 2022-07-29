package calebzhou.rdimc.celestech.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.PalettedContainer;
import net.minecraft.world.level.chunk.PalettedContainerRO;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.material.Fluids;

public class WorldUtils {
    /*public static void changeBiome(BlockPos bpos,ServerLevel world,Biome biome){
        ChunkAccess chunk = world.getChunk(bpos.getX()>>4,bpos.getZ()>>4);
        int sectionYindex = (bpos.getY()+64)>>4;
        PalettedContainer<Holder<Biome>> biomeArray = (PalettedContainer<Holder<Biome>>) chunk.getSection(sectionYindex).getBiomes();
        int mx=bpos.getX()&3;
        int my=bpos.getY()&3;
        int mz=bpos.getZ()&3;
        biomeArray.getAndSet(mx,my,mz, (biome));
        chunk.setUnsaved(true);
    }*/
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
