package calebzhou.rdimc.celestech.mixin;

import calebzhou.rdimc.celestech.event.PlayerBreakBlockCallback;
import calebzhou.rdimc.celestech.event.RegisterCommandsCallback;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.ActionResult;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CommandManager.class)
public abstract class RegisterCommandsMixin {
    @Shadow
    @Final
    private CommandDispatcher<ServerCommandSource> dispatcher;

    @Inject(at = @At(
            value = "FIELD",target = "net/minecraft/server/command/CommandManager.dispatcher : Lcom/mojang/brigadier/CommandDispatcher;",shift = At.Shift.AFTER),
            method = "<init>(Lnet/minecraft/server/command/CommandManager$RegistrationEnvironment;)V")
    private void registerz(CommandManager.RegistrationEnvironment environment,CallbackInfo callbackInfo) {
        ActionResult result = RegisterCommandsCallback.EVENT.invoker().register(dispatcher,environment);
        if(result == ActionResult.FAIL)
            callbackInfo.cancel();
    }
}
