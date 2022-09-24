package calebzhou.rdi.core.server.mixin.gameplay;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ComposterBlock.class)
public abstract class mDumpDirtFromComposter {
  //堆肥桶倒出来一个草方块，instead of 骨粉
        @Inject(method = "extractProduce",
        at=@At(value = "INVOKE",target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z")
        ,locals = LocalCapture.CAPTURE_FAILSOFT)
        private static void extractGrassBlockFromComposterBlock(BlockState blockState, Level level, BlockPos blockPos, CallbackInfoReturnable<BlockState> cir, float f, double d, double e, double g, ItemEntity itemEntityBoneMeal){
            ItemEntity itemEntity = new ItemEntity(level, (double)blockPos.getX() + d, (double)blockPos.getY() + e, (double)blockPos.getZ() + g, new ItemStack(Items.GRASS_BLOCK));
            itemEntity.setDefaultPickUpDelay();
            level.addFreshEntity(itemEntity);
        }

}
