package calebzhou.rdimc.celestech.mixin.mobs;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(MobEntity.class)
public abstract class MixinMobNoDayBurn extends LivingEntity {

    protected MixinMobNoDayBurn(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    /**
     * 所有怪物不受白天影响
     * @author
     */
    @Overwrite
    public boolean isAffectedByDaylight() {
        if(getWorld().isDay() && getType()==EntityType.PHANTOM)
            return true;
         return false;
    }
}
