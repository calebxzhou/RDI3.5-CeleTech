package calebzhou.rdimc.celestech.mixin;

import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(MobEntity.class)
public class MobEntityMixin {
    /**
     * 所有怪物不受白天影响
     * @author
     */
    @Overwrite
    public boolean isAffectedByDaylight() {
         return false;
    }
}
