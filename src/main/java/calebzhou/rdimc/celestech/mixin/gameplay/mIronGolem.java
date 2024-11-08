package calebzhou.rdimc.celestech.mixin.gameplay;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.IronGolem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(IronGolem.class)
public abstract class mIronGolem {

    //铁傀儡增加血量
    @Overwrite
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 200.0)
                .add(Attributes.MOVEMENT_SPEED, 0.6)
                .add(Attributes.KNOCKBACK_RESISTANCE, 2.0)
                .add(Attributes.ATTACK_DAMAGE, 15.0);
    }
    //什么都攻击
    @Overwrite
    public boolean canAttackType(EntityType<?> entityType) {
        return true;
    }


}
