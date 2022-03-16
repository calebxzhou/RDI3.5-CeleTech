package calebzhou.rdimc.celestech.mixin.mobs;

import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = {EnderMan.class})
public abstract class MixinHarderEnderman {
    /**
     * @author 拿了方块也会被despawn
     */
    @Overwrite
    public boolean requiresCustomPersistence() {
        return false;
    }

}