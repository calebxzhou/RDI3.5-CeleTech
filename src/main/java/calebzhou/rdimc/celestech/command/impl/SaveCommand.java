package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.RDICeleTech;
import calebzhou.rdimc.celestech.command.RdiCommand;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class SaveCommand implements RdiCommand {
    @Override
    public String getName() {
        return "SAVE";
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getExecution() {
        return Commands.literal(getName()).executes(c->{
            RDICeleTech.getServer().saveEverything(true, true, true);
            c.getSource().sendSuccess(Component.literal("成功"),true);
            return 1;
        });
    }
}
