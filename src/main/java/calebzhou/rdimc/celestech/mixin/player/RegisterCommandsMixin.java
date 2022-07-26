package calebzhou.rdimc.celestech.mixin.player;

import calebzhou.rdimc.celestech.event.PlayerBreakBlockCallback;
import calebzhou.rdimc.celestech.event.RegisterCommandsCallback;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandBuildContext;
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
            value = "INVOKE",target = "Lcom/mojang/brigadier/CommandDispatcher;setConsumer(Lcom/mojang/brigadier/ResultConsumer;)V"),
            method = "<init>(Lnet/minecraft/commands/Commands$CommandSelection;Lnet/minecraft/commands/CommandBuildContext;)V")
    private void registerz(Commands.CommandSelection commandSelection, CommandBuildContext commandBuildContext, CallbackInfo ci) {



        InteractionResult result = RegisterCommandsCallback.EVENT.invoker().register(dispatcher,commandSelection);
        if(result == InteractionResult.FAIL)
            ci.cancel();
    }
}
