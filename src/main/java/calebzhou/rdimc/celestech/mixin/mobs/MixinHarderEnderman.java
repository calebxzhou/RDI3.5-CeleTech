package calebzhou.rdimc.celestech.mixin.mobs;

import calebzhou.rdimc.celestech.constant.WorldConstants;
import calebzhou.rdimc.celestech.utils.PlayerUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.tag.Tag;
import net.minecraft.tag.TagKey;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = {EndermanEntity.PickUpBlockGoal.class})
public abstract class MixinHarderEnderman {
    @Final
    @Shadow
    private EndermanEntity enderman;
    //小黑能够捡起所有物品
    @Redirect(
            method = "Lnet/minecraft/entity/mob/EndermanEntity$PickUpBlockGoal;tick()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/BlockState;isIn(Lnet/minecraft/tag/TagKey;)Z"
            )
    )
    private boolean everythingpickup(BlockState instance, TagKey tagKey){
        if(!PlayerUtils.getDimensionName(enderman).equals(WorldConstants.DEFAULT_WORLD))
            return false;
        if(instance.getBlock() == Blocks.WATER || instance.getBlock() == Blocks.LAVA)
            return false;
        if(instance.getBlock() == Blocks.CHEST)
            return false;

        else  return true;
    }
}
@Mixin(EndermanEntity.class)
abstract class MixinHarderEnderman2 {
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