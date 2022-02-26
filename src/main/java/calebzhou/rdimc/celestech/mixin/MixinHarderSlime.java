package calebzhou.rdimc.celestech.mixin;

import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//增加史莱姆移动速度
@Mixin(SlimeEntity.class)
public abstract class MixinHarderSlime {
    @Inject(method = "Lnet/minecraft/entity/mob/SlimeEntity;setSize(IZ)V",
            at = @At(value = "TAIL"))
    private void setSize(int size, boolean heal, CallbackInfo ci){
        int i = MathHelper.clamp((int)size, (int)1, (int)127);
        SlimeEntity slime = ((SlimeEntity) ((Object) (this)));
        slime.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(i*i*2);
        slime.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(0.8F + 0.1F * (float)i);
        slime.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(i*2);
        slime.setHealth(i*i*2);
    }

}
