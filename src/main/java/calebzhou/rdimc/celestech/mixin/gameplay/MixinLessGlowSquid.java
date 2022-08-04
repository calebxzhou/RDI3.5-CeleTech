package calebzhou.rdimc.celestech.mixin.gameplay;

import calebzhou.rdimc.celestech.RDICeleTech;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.GlowSquid;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(GlowSquid.class)
public class MixinLessGlowSquid {

    /**
     * 发光鱿鱼减少生成
     * @author
     */
    @Overwrite
    public static boolean checkGlowSquideSpawnRules(EntityType<? extends LivingEntity> entityType, ServerLevelAccessor world
            , MobSpawnType mobSpawnType, BlockPos pos, RandomSource randomSource) {
        return world.getBlockState(pos).is(Blocks.WATER) && pos.getY() <= world.getSeaLevel() - 33
                &&
                RDICeleTech.RANDOM.nextInt(0,12)==0;
    }

}
