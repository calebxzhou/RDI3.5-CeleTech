package calebzhou.rdimc.celestech.mixin.mobs;

import calebzhou.rdimc.celestech.RDICeleTech;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.GlowSquidEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Random;

@Mixin(GlowSquidEntity.class)
public class MixinLessGlowSquid {

    /**
     * 发光鱿鱼减少生成
     * @author
     */
    @Overwrite
    public static boolean canSpawn(EntityType<? extends LivingEntity> type, ServerWorldAccess world, SpawnReason reason, BlockPos pos, Random random) {
        return world.getBlockState(pos).isOf(Blocks.WATER) && pos.getY() <= world.getSeaLevel() - 33
                &&
                RDICeleTech.RANDOM.nextInt(0,12)==0;
    }

}
