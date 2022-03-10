package calebzhou.rdimc.celestech.mixin.mobs;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ZombieEntity.class)
public abstract class MixinHarderZombie extends MobEntity {
    protected MixinHarderZombie(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
    }

    /**
     * @author
     */
    @Overwrite
    public static DefaultAttributeContainer.Builder createZombieAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 35.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.40f)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 4.0)
                .add(EntityAttributes.GENERIC_ARMOR, 2.0)
                .add(EntityAttributes.ZOMBIE_SPAWN_REINFORCEMENTS)
                .add(EntityAttributes.GENERIC_MAX_HEALTH,50);
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
