package calebzhou.rdimc.celestech.mixin.gameplay;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(NetherPortalBlock.class)
public class MixinMoreZombiePiglin {
    /**
     * 更多的猪人！
     * @author
     */
    @Overwrite
    public void randomTick(BlockState blockState, ServerLevel world, BlockPos pos, RandomSource randomSource) {
        if (world.dimensionType().natural() && world.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING) && randomSource.nextInt(1000) < 5) {
            ZombifiedPiglin entity;
            while (world.getBlockState(pos).getBlock() == Blocks.NETHER_PORTAL) {
                pos = pos.below();
            }
            if (world.getBlockState(pos).isValidSpawn(world, pos, EntityType.ZOMBIFIED_PIGLIN) && (entity = EntityType.ZOMBIFIED_PIGLIN.spawn(world, null, null, null, pos.above(), MobSpawnType.STRUCTURE, false, false)) != null) {
                entity.setPortalCooldown();
            }
        }
    }
}
