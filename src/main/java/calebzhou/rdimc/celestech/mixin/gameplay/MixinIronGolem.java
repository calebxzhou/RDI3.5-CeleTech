package calebzhou.rdimc.celestech.mixin.gameplay;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(IronGolem.class)
public abstract class MixinIronGolem {

    //铁傀儡增加血量
    @Overwrite
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 200.0).add(Attributes.MOVEMENT_SPEED, 0.4).add(Attributes.KNOCKBACK_RESISTANCE, 2.0).add(Attributes.ATTACK_DAMAGE, 15.0);
    }
    //什么都攻击
    @Overwrite
    public boolean canAttackType(EntityType<?> entityType) {
        return true;
    }

    @Shadow
    public abstract boolean isPlayerCreated();

}
