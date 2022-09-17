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
    private static void inPortalDimension(Level level, CallbackInfoReturnable<Boolean> cir) {
        if(WorldUtils.isInIsland2(level)){
            cir.setReturnValue(true);
        }
    }
}
@Mixin(Entity.class)
class mIsland2NetherPortal2{
    /*@Inject(method = "findDimensionEntryPoint",at = @At("HEAD"), cancellable = true)
    private  void inPortalDimension(ServerLevel serverLevel, CallbackInfoReturnable<PortalInfo> cir) {
        if(WorldUtils.isInIsland2(serverLevel)){
            Vec3i netherPos = WorldUtils.getIsland2ToNetherPos(WorldUtils.getIsland2IdInt(serverLevel));
            cir.setReturnValue(new PortalInfo(Vec3.atCenterOf(netherPos),new Vec3(0,0,0),0f,0f));
        }
    }*/
}
