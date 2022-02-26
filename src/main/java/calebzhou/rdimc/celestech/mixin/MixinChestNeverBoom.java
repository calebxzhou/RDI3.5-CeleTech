package calebzhou.rdimc.celestech.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ExplosionBehavior.class)
public class MixinChestNeverBoom {
    /**
     * @author 有数据的方块不会被炸
     */
    @Overwrite
    public boolean canDestroyBlock(Explosion explosion, BlockView world, BlockPos pos, BlockState state, float power) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if(blockEntity==null)
            return true;
        if(blockEntity.getType()== BlockEntityType.CHEST)
            return false;
        return true;
    }
}
