package calebzhou.rdimc.celestech.mixin.gameplay;

import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(Zombie.class)
public abstract class MixinZombie extends Mob {
    protected MixinZombie(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }

    /**
     * @author
     */
    @Overwrite
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 35.0)
                .add(Attributes.MOVEMENT_SPEED, 0.40f)
                .add(Attributes.ATTACK_DAMAGE, 4.0)
                .add(Attributes.ARMOR, 2.0)
                .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE)
                .add(Attributes.MAX_HEALTH,50);
    }
            /**
     * @author
     */
    @Overwrite
    public void populateDefaultEquipmentSlots(RandomSource randomSource, DifficultyInstance difficulty) {
        super.populateDefaultEquipmentSlots(randomSource, difficulty);
             this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));

    }
}
