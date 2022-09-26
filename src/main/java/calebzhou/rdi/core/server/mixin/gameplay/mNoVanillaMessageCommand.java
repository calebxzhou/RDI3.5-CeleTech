package calebzhou.rdi.core.server.mixin.gameplay;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Created by calebzhou on 2022-09-26,21:02.
 */
@Mixin(Commands.class)
public class mNoVanillaMessageCommand {
	@Redirect(method = "<init>",at=@At(value = "INVOKE",target = "Lnet/minecraft/server/commands/MsgCommand;register(Lcom/mojang/brigadier/CommandDispatcher;)V"))
	private void fuckMSMessage(CommandDispatcher<CommandSourceStack> dispatcher){}
}
