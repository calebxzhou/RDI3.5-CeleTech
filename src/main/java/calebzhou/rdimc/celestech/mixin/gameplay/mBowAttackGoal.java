package calebzhou.rdimc.celestech.mixin.gameplay;

import net.minecraft.world.entity.ai.goal.RangedBowAttackGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(RangedBowAttackGoal.class)
public class mBowAttackGoal {
    @ModifyConstant(method = "tick",constant = @Constant(intValue = 20,ordinal = 2))
    private int moreFrequentAttack(int constant){

        return 2;
    }
}
