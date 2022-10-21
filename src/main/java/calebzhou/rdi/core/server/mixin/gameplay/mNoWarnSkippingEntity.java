package calebzhou.rdi.core.server.mixin.gameplay;


import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(EntityType.class)
public abstract class mNoWarnSkippingEntity {
	@Shadow
	public static Optional<EntityType<?>> by(CompoundTag compound) {
		return null;
	}

	@Overwrite
	public static Optional<Entity> create(CompoundTag compound, Level level) {
		return Util.ifElse(by(compound).map((entityType) -> entityType.create(level)), (entity) -> entity.load(compound), () -> {

		});
	}
}
