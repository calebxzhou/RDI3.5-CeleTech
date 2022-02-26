package calebzhou.rdimc.celestech.mixin.mobs;

import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.world.gen.PhantomSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

//幻翼加速
@Mixin(PhantomEntity.class)
public class MixinHarderPhantom {

    @ModifyConstant(
            method = "Lnet/minecraft/entity/mob/PhantomEntity;onSizeChanged()V"
            ,constant = @Constant(intValue = 6)
    )
    private int changeDamange(int constant){
        return 15;
    }

}
@Mixin(PhantomEntity.PhantomMoveControl.class)
class PhantomMoveMixin{

    private final float targetSpeed = 0.4F;

    @ModifyConstant(
            method = "Lnet/minecraft/entity/mob/PhantomEntity$PhantomMoveControl;tick()V",
            constant = @Constant
                    (floatValue = 0.1F)
    )
    private float changeSpd1(float constant){
        return this.targetSpeed*3;
    }
    @ModifyConstant(
            method = "Lnet/minecraft/entity/mob/PhantomEntity$PhantomMoveControl;tick()V",
            constant = @Constant
                    (doubleValue = 0.2D)
    )
    private double changeSpd2(double constant){
        return 0.8D*2;
    }
}
@Mixin(PhantomSpawner.class)
class PhantomSpawnerMixin{
    @ModifyConstant(
            method = "Lnet/minecraft/world/gen/PhantomSpawner;spawn(Lnet/minecraft/server/world/ServerWorld;ZZ)I"
            ,constant = @Constant(intValue = 1,ordinal = 1)

    )
    private int changeAmount(int c){
        return 26;
    }
}