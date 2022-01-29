package calebzhou.rdimc.celestech.mixin;

import calebzhou.rdimc.celestech.goal.EntityBreakBlockGoal;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ZombieEntity.class)
public abstract class ZombieEntityMixin extends MobEntity {
    protected ZombieEntityMixin(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyConstant(
            method = "Lnet/minecraft/entity/mob/ZombieEntity;createZombieAttributes()Lnet/minecraft/entity/attribute/DefaultAttributeContainer$Builder;",
            constant = @Constant(doubleValue = 0.23000000417232513D)
    )
    private static double changeSpeed(double spd){
        return 0.30D;
    }

    @ModifyConstant(
            method = "Lnet/minecraft/entity/mob/ZombieEntity;createZombieAttributes()Lnet/minecraft/entity/attribute/DefaultAttributeContainer$Builder;",
            constant = @Constant(doubleValue = 3.0D)
    )
    private static double changeAtk(double spd){
        return 5.0D;
    }

            /**
     * @author
     */
    @Overwrite
    public void initEquipment(LocalDifficulty difficulty) {
        super.initEquipment(difficulty);
             this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));

    }
}
