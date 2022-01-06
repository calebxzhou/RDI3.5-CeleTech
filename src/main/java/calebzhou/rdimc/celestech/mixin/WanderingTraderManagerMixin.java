package calebzhou.rdimc.celestech.mixin;

import net.minecraft.world.WanderingTraderManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Random;

@Mixin(WanderingTraderManager.class)
public abstract class WanderingTraderManagerMixin {
    @ModifyConstant(method = "Lnet/minecraft/world/WanderingTraderManager;spawn(Lnet/minecraft/server/world/ServerWorld;ZZ)I",
    constant = @Constant(intValue = 24000))
    private int modifyConstSpawnDelay(int constant){
        return 12000;
    }

    @ModifyConstant(method = "Lnet/minecraft/world/WanderingTraderManager;spawn(Lnet/minecraft/server/world/ServerWorld;ZZ)I",
            constant = @Constant(intValue = 75))
    private int modifyConstMaxPercent(int constant){
        return 100;
    }
    @Redirect(method = "Lnet/minecraft/world/WanderingTraderManager;trySpawn(Lnet/minecraft/server/world/ServerWorld;)Z",
            at = @At(value = "INVOKE",target = "Ljava/util/Random;nextInt(I)I"))
    private int modNext(Random rand, int bound){
        return 0;
    }

}
