package calebzhou.rdimc.celestech.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

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
        return 0.50D;
    }

    @ModifyConstant(
            method = "Lnet/minecraft/entity/mob/ZombieEntity;createZombieAttributes()Lnet/minecraft/entity/attribute/DefaultAttributeContainer$Builder;",
            constant = @Constant(doubleValue = 3.0D)
    )
    private static double changeAtk(double spd){
        return 6.0D;
    }

    /**
     * @author
     */
    @Overwrite
    public void initEquipment(LocalDifficulty difficulty) {
        super.initEquipment(difficulty);
            int i = this.random.nextInt(10);
            switch (i){
                case 0 -> this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.STONE_PICKAXE));
                case 1 -> this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_PICKAXE));
                case 2 -> this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.STONE_AXE));
                case 3 -> this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_AXE));
                case 4 -> this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
                case 5 -> this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
                default -> {}
            }

    }
}
