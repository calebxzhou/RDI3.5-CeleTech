package calebzhou.rdimc.celestech.mixin.mobs;

import calebzhou.rdimc.celestech.RDICeleTech;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(NetherPortalBlock.class)
public class MixinMoreZombiePiglin {
    /**
     * 更多的猪人！
     * @author
     */
    @Overwrite
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
        if (world.dimensionType().natural() && world.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING) && RDICeleTech.RANDOM.nextInt(1000) < world.getDifficulty().getId()) {
            ZombifiedPiglin entity;
            while (world.getBlockState(pos).is((NetherPortalBlock)(Object)this)) {
                pos = pos.below();
            }
            if (world.getBlockState(pos).isValidSpawn(world, pos, EntityType.ZOMBIFIED_PIGLIN) && (entity = EntityType.ZOMBIFIED_PIGLIN.spawn(world, null, null, null, pos.above(), MobSpawnType.STRUCTURE, false, false)) != null) {
                entity.setPortalCooldown();
            }
        }
    }
}
