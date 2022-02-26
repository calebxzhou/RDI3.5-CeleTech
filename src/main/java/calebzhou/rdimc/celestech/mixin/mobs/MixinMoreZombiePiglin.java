package calebzhou.rdimc.celestech.mixin.mobs;

import calebzhou.rdimc.celestech.RDICeleTech;
import net.minecraft.block.BlockState;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.ZombifiedPiglinEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Random;

@Mixin(NetherPortalBlock.class)
public class MixinMoreZombiePiglin {
    /**
     * 更多的猪人！
     * @author
     */
    @Overwrite
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (world.getDimension().isNatural() && world.getGameRules().getBoolean(GameRules.DO_MOB_SPAWNING) && RDICeleTech.RANDOM.nextInt(1000) < world.getDifficulty().getId()) {
            ZombifiedPiglinEntity entity;
            while (world.getBlockState(pos).isOf((NetherPortalBlock)(Object)this)) {
                pos = pos.down();
            }
            if (world.getBlockState(pos).allowsSpawning(world, pos, EntityType.ZOMBIFIED_PIGLIN) && (entity = EntityType.ZOMBIFIED_PIGLIN.spawn(world, null, null, null, pos.up(), SpawnReason.STRUCTURE, false, false)) != null) {
                entity.resetNetherPortalCooldown();
            }
        }
    }
}
