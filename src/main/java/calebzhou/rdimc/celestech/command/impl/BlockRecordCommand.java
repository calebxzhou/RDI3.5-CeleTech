package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.command.RdiCommand;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.thread.RdiHttpPlayerRequest;
import calebzhou.rdimc.celestech.thread.RdiHttpRequest;
import calebzhou.rdimc.celestech.thread.RdiRequestThread;
import calebzhou.rdimc.celestech.utils.TextUtils;
import calebzhou.rdimc.celestech.utils.WorldUtils;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;

public class BlockRecordCommand extends RdiCommand {
    public BlockRecordCommand() {
        super("block-record");
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getExecution() {
        return baseArgBuilder.then(
                Commands.argument("blockPos", BlockPosArgument.blockPos())
                        .executes(this::exec)
        );
    }

    private int exec(CommandContext<CommandSourceStack> context) {
        ServerPlayer player = context.getSource().getPlayer();
        BlockPos blockPos = null;
        try {
            blockPos = BlockPosArgument.getLoadedBlockPos(context, "blockPos");
        } catch (CommandSyntaxException e) {
            TextUtils.sendChatMessage(player, MessageType.ERROR,"方块位置格式错误！"+e.getMessage());
        }
        RdiRequestThread.addTask(new RdiHttpPlayerRequest(
                RdiHttpRequest.Type.get,
                player,
                resp->{

                },
                "record/block",
                "x="+blockPos.getX(),"y="+blockPos.getY(),"z="+blockPos.getZ(),"dim="+ WorldUtils.getDimensionName(player.level)
        ));
        return 1;
    }
}
