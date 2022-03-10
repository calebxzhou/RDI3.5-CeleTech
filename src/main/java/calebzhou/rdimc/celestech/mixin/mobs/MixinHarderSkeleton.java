package calebzhou.rdimc.celestech.mixin.mobs;

import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.HostileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(AbstractSkeletonEntity.class)
public abstract class MixinHarderSkeleton {
    @ModifyConstant(
            method = "Lnet/minecraft/entity/mob/AbstractSkeletonEntity;updateAttackType()V",
            constant = @Constant(intValue = 20)
    )
    private static int changeAtkSpeed(int spd){
        return 2;
    }
    /**
     * @author
     */
    @Overwrite
    public static DefaultAttributeContainer.Builder createAbstractSkeletonAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.4)
                .add(EntityAttributes.GENERIC_MAX_HEALTH,50);
    }
}
