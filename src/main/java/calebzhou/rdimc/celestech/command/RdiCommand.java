package calebzhou.rdimc.celestech.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;

public interface RdiCommand {
    String getName();
    LiteralArgumentBuilder<CommandSourceStack> getExecution();
}
