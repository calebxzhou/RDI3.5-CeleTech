package calebzhou.rdimc.celestech.goal;

import net.minecraft.block.Block;
import net.minecraft.entity.ai.NavigationConditions;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldEvents;

public class EntityBreakBlockGoal extends Goal {
    protected MobEntity entity;
    protected BlockPos blockPos;
    private boolean shouldStop;
    protected int breakProgress;
    protected int prevBreakProgress;
    protected int maxProgress;
    private float offsetX;
    private float offsetZ;
    private static final int MIN_MAX_PROGRESS = 80;

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
            return false;
        } else if (!this.entity.horizontalCollision) {
            return false;
        }
        if (entity.getStackInHand(Hand.MAIN_HAND).isEmpty()) {
            return false;
        }else
            return true;
    }
    @Override
    public void start() {
        this.shouldStop = false;
        this.offsetX = (float)((double)this.blockPos.getX() + 0.5D - this.entity.getX());
        this.offsetZ = (float)((double)this.blockPos.getZ() + 0.5D - this.entity.getZ());
    }
    protected int getMaxProgress() {
        return Math.max(MIN_MAX_PROGRESS, this.maxProgress);
    }
    @Override
    public void tick() {
        float f = (float)((double)this.blockPos.getX() + 0.5D - this.entity.getX());
        float g = (float)((double)this.blockPos.getZ() + 0.5D - this.entity.getZ());
        float h = this.offsetX * f + this.offsetZ * g;
        if (h < 0.0F) {
            this.shouldStop = true;
        }


        if (this.entity.getRandom().nextInt(20) == 0) {
            this.entity.world.syncWorldEvent(WorldEvents.ZOMBIE_ATTACKS_WOODEN_DOOR, this.blockPos, 0);
            if (!this.entity.handSwinging) {
                this.entity.swingHand(this.entity.getActiveHand());
            }
        }
        ++this.breakProgress;
        int i = (int)((float)this.breakProgress / (float)this.getMaxProgress() * 10.0F);
        if (i != this.prevBreakProgress) {
            this.entity.world.setBlockBreakingInfo(this.entity.getId(), this.blockPos, i);
            this.prevBreakProgress = i;
        }
        if (this.breakProgress == this.getMaxProgress()) {
            this.entity.world.removeBlock(this.blockPos, false);
            this.entity.world.syncWorldEvent(WorldEvents.ZOMBIE_BREAKS_WOODEN_DOOR, this.blockPos, 0);
            this.entity.world.syncWorldEvent(WorldEvents.BLOCK_BROKEN, this.blockPos, Block.getRawIdFromState(this.entity.world.getBlockState(this.blockPos)));
        }
    }
}
