package calebzhou.rdimc.celestech.goal;

import calebzhou.rdimc.celestech.RDICeleTech;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.DoorBlock;
import net.minecraft.entity.ai.NavigationConditions;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.ai.pathing.PathNode;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldEvents;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityBreakBlockGoal extends Goal {
    private static final Logger log = RDICeleTech.LOGGER;
    protected MobEntity entity;
    protected BlockPos blockPos;
    protected BlockPos prevBlockPos;
    private boolean shouldStop;
    private boolean blockValid;
    protected int breakProgress=0;
    protected int prevBreakProgress=0;
    protected int maxProgress=80;
    private float offsetX;
    private float offsetZ;

    public EntityBreakBlockGoal(MobEntity entity) {
        this.entity = entity;
    }

    @Override
    public boolean shouldRunEveryTick() {
        return true;
    }

    @Override
    public boolean shouldContinue() {
        return !this.shouldStop;
    }

    @Override
    public boolean canStart() {
        if (!NavigationConditions.hasMobNavigation(this.entity)) {
           // log.info("no navigation");
            return false;

        }/* else if (!this.entity.horizontalCollision) {
            log.info("no collision");
            return false;
        }*/
        if (entity.getStackInHand(Hand.MAIN_HAND).isEmpty()) {
         //   log.info("no hand item");
            return false;
        }
        MobNavigation mobNavigation = (MobNavigation) this.entity.getNavigation();
        //TODO 怪的方向决定破坏方向
        setBlockPos();
        Path path = mobNavigation.findPathTo(this.blockPos,1);
        //log.info("blockPos_PATH1 . "+blockPos.toShortString());
        if(path != null){

            /*for(int i = 0; i < Math.min(path.getCurrentNodeIndex() + 2, path.getLength()); ++i) {
                PathNode pathNode = path.getNode(i);
                this.blockPos = new BlockPos(pathNode.x, pathNode.y + 1, pathNode.z);
                log.info("blockPos_PATH2 . "+blockPos.toShortString());
                double dist = this.entity.squaredDistanceTo(this.blockPos.getX(), this.entity.getY(), this.blockPos.getZ());
                log.info("dist "+dist);
                if (dist <= 2.25D) {
                    log.info("block valid : "+blockValid);
                    return  true;
                    *//*this.blockValid = this.entity.world.getBlockState(blockPos).getBlock() != Blocks.AIR;
                    if (this.blockValid) {
                        return true;
                    }*//*
                }
            }*/


            this.blockValid=true;
            return this.blockValid;
        }else
           // log.info("null path");
            return false;
    }
    @Override
    public void start() {
        this.shouldStop = false;
        this.offsetX = (float)((double)this.blockPos.getX() + 0.5D - this.entity.getX());
        this.offsetZ = (float)((double)this.blockPos.getZ() + 0.5D - this.entity.getZ());
    }
    @Override
    public void tick() {
        float f = (float)((double)this.blockPos.getX() + 0.5D - this.entity.getX());
        float g = (float)((double)this.blockPos.getZ() + 0.5D - this.entity.getZ());
        float h = this.offsetX * f + this.offsetZ * g;
        if (h < 0.0F) {
            this.shouldStop = true;
        }

        int ran=this.entity.getRandom().nextInt(2);
        //log.info(ran+" : random");
        if (ran == 0) {
            this.entity.world.syncWorldEvent(WorldEvents.ZOMBIE_ATTACKS_WOODEN_DOOR, this.blockPos, 0);
            if (!this.entity.handSwinging) {
                this.entity.swingHand(this.entity.getActiveHand());
            }
        }
        setBlockPos();



        ++this.breakProgress;
        //log.info("break progress "+breakProgress);
        this.entity.world.setBlockBreakingInfo(this.entity.getId(), this.blockPos, (int)((float)this.breakProgress / (float)this.maxProgress * 10.0F));
        /*int i = (int)((float)this.breakProgress / (float)this.getMaxProgress() * 10.0F);
        log.info("i "+i);
        if (i != this.prevBreakProgress) {
            this.entity.world.setBlockBreakingInfo(this.entity.getId(), this.blockPos, i);
            this.prevBreakProgress = i;
        }*/
        if (this.breakProgress >= this.maxProgress) {
            
            if(this.entity.world.getBlockState(blockPos).getBlock()!=Blocks.OBSIDIAN){
               // log.info("break block success "+blockPos.toShortString());
                this.prevBlockPos = blockPos;
                this.entity.world.setBlockState(this.blockPos,Blocks.AIR.getDefaultState());
            }else
                //log.info("no break obisdian "+blockPos.toShortString());
            breakProgress=0;
        }
        //throw new NullPointerException("test");
    }
    private void setBlockPos(){
        Direction facing = this.entity.getHorizontalFacing();
        if(this.prevBlockPos==null)
            this.blockPos = this.entity.getBlockPos().up();
        else
            this.blockPos = this.entity.getBlockPos();
        if(facing==Direction.EAST)
            this.blockPos = this.blockPos.add(1,0,0);
        else if(facing==Direction.SOUTH)
            this.blockPos = this.blockPos.add(0,0,1);
        else if(facing==Direction.WEST)
            this.blockPos = this.blockPos.add(-1,0,0);
        else if(facing==Direction.NORTH)
            this.blockPos = this.blockPos.add(0,0,-1);
    }
}
