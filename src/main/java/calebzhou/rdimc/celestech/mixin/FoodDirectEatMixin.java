package calebzhou.rdimc.celestech.mixin;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(Item.class)
public class FoodDirectEatMixin {
    /**
     * @author
     * 食物直接吃 没有延迟
     */
    @Overwrite
    public int getMaxUseTime(ItemStack stack) {
        if (stack.getItem().isFood()) {
            return 1;
        } else {
            return 0;
        }
    }
}
