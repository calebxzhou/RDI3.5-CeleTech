package calebzhou.rdimc.celestech.mixin;

import calebzhou.rdimc.celestech.RDICeleTech;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

//恶魂 增强
@Mixin(GhastEntity.class)
public class GhastEntityMixin {
    @ModifyConstant(
            method = "Lnet/minecraft/entity/mob/GhastEntity;createGhastAttributes()Lnet/minecraft/entity/attribute/DefaultAttributeContainer$Builder;"
            ,constant = @Constant(doubleValue = 10.0D)
    )
    private static double change(double d){
        return 25.0D;
    }
    /**
     * @author
     */
    @Overwrite
    public static boolean canSpawn(EntityType<GhastEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return RDICeleTech.RANDOM.nextInt(5) == 0 && GhastEntity.canMobSpawn(type, world, spawnReason, pos, random);
    }
}
@Mixin(GhastEntity.GhastMoveControl.class)
class GhastMoveMixin{
    @ModifyConstant(
            method = "Lnet/minecraft/entity/mob/GhastEntity$GhastMoveControl;tick()V"
            ,constant = @Constant(doubleValue = 0.1D)
    )
    private static double change(double d){
        return 0.2D;
    }
}
@Mixin(GhastEntity.FlyRandomlyGoal.class)
class GhastFlyMixin{
    @ModifyConstant(
            method = "Lnet/minecraft/entity/mob/GhastEntity$FlyRandomlyGoal;start()V"
            ,constant = @Constant(doubleValue = 1.0D)
    )
    private static double change(double d){
        return 1.5D;
    }
}
@Mixin(GhastEntity.ShootFireballGoal.class)
class GhastShootMixin{
    @Shadow @Mutable
    public int cooldown;
    @Shadow @Final
    private GhastEntity ghast;

    /*@ModifyConstant(
            method = "Lnet/minecraft/entity/mob/GhastEntity$ShootFireballGoal;tick()V"
            ,constant = @Constant(intValue = 20)
    )
    private static int changeCD(int constant){
        return 11;
    }*/
    @ModifyConstant(
            method = "Lnet/minecraft/entity/mob/GhastEntity$ShootFireballGoal;tick()V"
            ,constant = @Constant(doubleValue = 4.0D)
    )
    private static double changeSped(double constant){
        return 5.0D;
    }

    /*@Inject(
            method = "Lnet/minecraft/entity/mob/GhastEntity$ShootFireballGoal;tick()V",
            at=@At("TAIL")

    )
    private void changeCd2(CallbackInfo ci){
        if(cooldown<=-40){
            cooldown=9;
            this.ghast.setShooting(true);
        }
    }*/
}

@Mixin(FireballEntity.class)
class FireballSpeedMixin{
    @Shadow @Mutable private int explosionPower=3;
}