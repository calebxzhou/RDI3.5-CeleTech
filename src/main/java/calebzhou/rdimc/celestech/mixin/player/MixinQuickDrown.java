package calebzhou.rdimc.celestech.mixin.player;

import calebzhou.rdimc.celestech.RDICeleTech;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(LivingEntity.class)
public class MixinQuickDrown {
    private static final int MAX_AIR = 300;
    /**
     * @author 更容易溺水
     */
    @Overwrite
    public int getNextAirUnderwater(int air) {
        int i = EnchantmentHelper.getRespiration((LivingEntity)(Object) this);
        if (i > 0 && RDICeleTech.RANDOM.nextInt(i + 1) > 0) {
            return air;
        }
        return air - 5;
    }
    /**
     * @author 更不容易恢复氧气
     */
    @Overwrite
    public int getNextAirOnLand(int air) {
        return Math.min(air + 2, MAX_AIR);
    }
}
