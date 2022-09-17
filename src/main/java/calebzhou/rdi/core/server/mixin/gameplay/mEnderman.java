package calebzhou.rdi.core.server.mixin.gameplay;

import net.minecraft.world.entity.monster.EnderMan;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;


//小黑增强
@Mixin(EnderMan.class)
public abstract class mEnderman {
    //拿了方块也会被despawn
    @Overwrite
    public boolean requiresCustomPersistence() {
        return false;
    }

}
