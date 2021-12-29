package calebzhou.rdimc.celestech.constant;

import net.minecraft.entity.damage.DamageSource;

public class AngrySaplingDamageSource extends DamageSource {
    public static DamageSource source = new AngrySaplingDamageSource().setExplosive();
    protected AngrySaplingDamageSource() {
        super("angry_sapling");
    }
}
