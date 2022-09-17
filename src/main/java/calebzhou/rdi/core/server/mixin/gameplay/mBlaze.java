package calebzhou.rdi.core.server.mixin.gameplay;


import net.minecraft.world.entity.monster.Blaze;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(Blaze.BlazeAttackGoal.class)
public class mBlaze {
    @ModifyConstant(method = "tick",constant = @Constant(intValue = 1,ordinal = 2))
    private int quickAttack(int i){
        return 20;
    }
}
