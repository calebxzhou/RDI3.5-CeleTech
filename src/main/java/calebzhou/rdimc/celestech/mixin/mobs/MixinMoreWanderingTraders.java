package calebzhou.rdimc.celestech.mixin.mobs;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Random;
import net.minecraft.world.entity.npc.WanderingTraderSpawner;

//更多的流浪商人
@Mixin(WanderingTraderSpawner.class)
public abstract class MixinMoreWanderingTraders {
    @ModifyConstant(method = "Lnet/minecraft/world/entity/npc/WanderingTraderSpawner;<init>(Lnet/minecraft/world/level/storage/ServerLevelData;)V",
    constant = @Constant(intValue = 24000))
    private int modifyConstSpawnDelay(int constant){
        return 12000;
    }

    @ModifyConstant(method = "Lnet/minecraft/world/entity/npc/WanderingTraderSpawner;tick(Lnet/minecraft/server/level/ServerLevel;ZZ)I",
            constant = @Constant(intValue = 75))
    private int modifyConstMaxPercent(int constant){
        return 100;
    }
    @Redirect(method = "Lnet/minecraft/world/entity/npc/WanderingTraderSpawner;spawn(Lnet/minecraft/server/level/ServerLevel;)Z",
            at = @At(value = "INVOKE",target = "Ljava/util/Random;nextInt(I)I"))
    private int modNext(Random rand, int bound){
        return 0;
    }

}
