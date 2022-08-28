package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.command.RdiCommand;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;

public class BlockRecordCommand extends RdiCommand {
    public BlockRecordCommand() {
        super("block-record");
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getExecution() {
        return baseArgBuilder;
    }
}
