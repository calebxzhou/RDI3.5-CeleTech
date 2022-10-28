package calebzhou.rdi.core.server.mixin.server;

import calebzhou.rdi.core.server.ticking.EntityTicking;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTickList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Consumer;

/**
 * Created  on 2022-10-26,11:31.
 */
@Mixin(Level.class)
public class mUseRdiEntityTicking {

	@Overwrite
	public <T extends Entity> void guardEntityTick(Consumer<T> consumerEntity, T entity) {
		EntityTicking.tick(consumerEntity,entity);
	}
}
@Mixin(ServerLevel.class)
class mUseRdiEntityTicking2{
	@Redirect(method = "tick",at=@At(value = "INVOKE",target = "Lnet/minecraft/world/level/entity/EntityTickList;forEach(Ljava/util/function/Consumer;)V"))
	private void asd(EntityTickList tickList, Consumer<Entity> entity){
		EntityTicking.handleTickListForEach(tickList,(ServerLevel)(Object)this);
	}
}
