package calebzhou.rdimc.celestech.mixin.world;

import net.minecraft.world.level.block.SweetBerryBushBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(SweetBerryBushBlock.class)
public class MixinImproveSweetBerryDamage {
    @ModifyConstant(method = "Lnet/minecraft/block/SweetBerryBushBlock;onEntityCollision(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/Entity;)V",
    constant = @Constant(floatValue = 1.0f))
    private static float modifyDamage(float f){
        return 5.5f;
    }
}
