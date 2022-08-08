package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.command.RdiCommand;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;

public class SaveCommand implements RdiCommand {
    @Override
    public String getName() {
        return "SAVE";
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getExecution() {
        return null;
    }
}
