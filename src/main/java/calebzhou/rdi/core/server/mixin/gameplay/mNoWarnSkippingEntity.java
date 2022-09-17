package calebzhou.rdi.core.server.mixin.gameplay;


import net.minecraft.world.entity.EntityType;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityType.class)
public class mNoWarnSkippingEntity {
    @Redirect(method = "method_17847",at=@At(value = "INVOKE",target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;)V"))
    private static void noWarn(Logger instance, String s, Object o){

    }
}
