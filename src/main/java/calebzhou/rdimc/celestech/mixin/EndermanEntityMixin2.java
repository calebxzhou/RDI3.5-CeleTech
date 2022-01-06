package calebzhou.rdimc.celestech.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.entity.mob.EndermanEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EndermanEntity.class)
abstract class EndermanEntityMixin2{
    //拿了方块也会被despawn
    @Redirect(
            method = "Lnet/minecraft/entity/mob/EndermanEntity;cannotDespawn()Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/mob/EndermanEntity;getCarriedBlock()Lnet/minecraft/block/BlockState;"
            )
    )
    private BlockState getCarry(EndermanEntity instance){
        return null;
    }

}