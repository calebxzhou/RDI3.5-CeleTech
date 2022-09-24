package calebzhou.rdi.core.server.mixin.gameplay;


import calebzhou.rdi.core.server.utils.WorldUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = {BaseFireBlock.class})
public class mIsland2NetherPortal {
    @Inject(method = "inPortalDimension",at = @At("HEAD"), cancellable = true)
    private static void islandWorldCanIgniteNetherPortal(Level level, CallbackInfoReturnable<Boolean> cir) {
        if(WorldUtils.isInIsland2(level)){
            cir.setReturnValue(true);
        }
    }
}

