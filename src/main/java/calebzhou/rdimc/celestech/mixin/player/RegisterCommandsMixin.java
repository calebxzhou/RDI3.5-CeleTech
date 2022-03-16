package calebzhou.rdimc.celestech.mixin.player;

import calebzhou.rdimc.celestech.event.PlayerBreakBlockCallback;
import calebzhou.rdimc.celestech.event.RegisterCommandsCallback;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.world.InteractionResult;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Commands.class)
public abstract class RegisterCommandsMixin {
    @Shadow
    @Final
    private CommandDispatcher<CommandSourceStack> dispatcher;

    @Inject(at = @At(
            value = "INVOKE",target = "Lcom/mojang/brigadier/CommandDispatcher;findAmbiguities(Lcom/mojang/brigadier/AmbiguityConsumer;)V"),
            method = "Lnet/minecraft/commands/Commands;<init>(Lnet/minecraft/commands/Commands$CommandSelection;)V")
    private void registerz(Commands.CommandSelection environment,CallbackInfo callbackInfo) {
        InteractionResult result = RegisterCommandsCallback.EVENT.invoker().register(dispatcher,environment);
        if(result == InteractionResult.FAIL)
            callbackInfo.cancel();
    }
}
