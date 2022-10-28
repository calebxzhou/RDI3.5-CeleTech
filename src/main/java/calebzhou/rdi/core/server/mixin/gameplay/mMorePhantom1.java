package calebzhou.rdi.core.server.mixin.gameplay;

import net.minecraft.world.entity.monster.Phantom;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

//幻翼加速
@Mixin(Phantom.class)
public class mMorePhantom1 {

    @ModifyConstant(
            method = "updatePhantomSizeInfo()V"
            ,constant = @Constant(intValue = 6)
    )
    private int changeDamange(int constant){
        return 15;
    }

}
