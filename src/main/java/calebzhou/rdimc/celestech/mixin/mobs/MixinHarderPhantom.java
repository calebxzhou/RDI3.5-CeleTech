package calebzhou.rdimc.celestech.mixin.mobs;

import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.level.levelgen.PhantomSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

//幻翼加速
@Mixin(Phantom.class)
public class MixinHarderPhantom {

    @ModifyConstant(
            method = "Lnet/minecraft/world/entity/monster/Phantom;updatePhantomSizeInfo()V"
            ,constant = @Constant(intValue = 6)
    )
    private int changeDamange(int constant){
        return 15;
    }

}
//幻翼变多
@Mixin(PhantomSpawner.class)
class PhantomSpawnerMixin{
    @ModifyConstant(
            method = "Lnet/minecraft/world/level/levelgen/PhantomSpawner;tick(Lnet/minecraft/server/level/ServerLevel;ZZ)I"
            ,constant = @Constant(intValue = 1,ordinal = 1)

    )
    private int changeAmount(int c){
        return 26;
    }
}