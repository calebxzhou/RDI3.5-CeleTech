package calebzhou.rdimc.celestech.mixin.server;

import calebzhou.rdimc.celestech.command.RdiCommand;
import calebzhou.rdimc.celestech.command.impl.*;
import com.mojang.brigadier.CommandDispatcher;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Commands.class)
public abstract class mRegisterCommands {
    private static final ObjectArrayList<RdiCommand> commands = new ObjectArrayList<>();
    static{
        commands.add(new CreateCommand());
        commands.add(new DeleteCommand());
        commands.add(new HomeCommand());
        commands.add(new InviteCommand());
        commands.add(new KickCommand());
        commands.add(new LocaCommand());
        commands.add(new SpawnCommand());
        commands.add(new TpaCommand());
        commands.add(new TpreqCommand());
        commands.add(new TpsCommand());
        commands.add(new StruCommand());
        commands.add(new EncryptCommand());
        //commands.add(new BioCommand());
    }
    @Shadow
    @Final
    private CommandDispatcher<CommandSourceStack> dispatcher;

    @Inject(at = @At(
            value = "INVOKE",target = "Lcom/mojang/brigadier/CommandDispatcher;setConsumer(Lcom/mojang/brigadier/ResultConsumer;)V"),
            method = "<init>(Lnet/minecraft/commands/Commands$CommandSelection;Lnet/minecraft/commands/CommandBuildContext;)V")
    private void registerz(Commands.CommandSelection commandSelection, CommandBuildContext commandBuildContext, CallbackInfo ci) {
        for (RdiCommand cmd : commands) {
            if (cmd.getExecution() != null) {
                dispatcher.register(cmd.getExecution());
            }
        }
    }
}
