package calebzhou.rdi.core.server.mixin.gameplay;


import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

//苦力怕更容易爆炸
@Mixin(Creeper.class)
public abstract class mCreeper {

    @Shadow @Mutable
    private int maxSwell = 50;
    @Shadow @Mutable
    private int explosionRadius = 5;

	@Overwrite
	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.4).add(Attributes.MAX_HEALTH,30);
	}

}
