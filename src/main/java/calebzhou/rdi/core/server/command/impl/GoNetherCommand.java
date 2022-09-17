package calebzhou.rdi.core.server.command.impl;

import calebzhou.rdi.core.server.RdiSharedConstants;
import calebzhou.rdi.core.server.command.RdiCommand;
import calebzhou.rdi.core.server.utils.PlayerUtils;
import calebzhou.rdi.core.server.utils.WorldUtils;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Blocks;

import static calebzhou.rdi.core.server.utils.PlayerUtils.*;

public class GoNetherCommand extends RdiCommand {
    public GoNetherCommand() {
        super("go-nether");
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getExecution() {
        return baseArgBuilder.executes(this::exec);
    }

    private int exec(CommandContext<CommandSourceStack> context) {
        ServerPlayer player = context.getSource().getPlayer();
        if(!isInIsland(player)){
            sendChatMessage(player, RESPONSE_ERROR,"只有在您的岛屿才能通过此功能前往地狱！");
            return 1;
        }
        if (player.experienceLevel<3) {
            sendChatMessage(player, RESPONSE_ERROR,"必须有3级经验才能前往地狱！");
            return 1;
        }
        Vec3i targetPos = WorldUtils.getIsland2ToNetherPos(WorldUtils.getIsland2IdInt(player.getLevel()));
        WorldUtils.placeBlock(WorldUtils.getNether(),new BlockPos(targetPos), Blocks.OBSIDIAN.defaultBlockState());
        teleport(player,WorldUtils.getNether() ,targetPos.getX(),targetPos.getY()+3,targetPos.getZ(),0,0);
        sendChatMessage(player, RESPONSE_SUCCESS,"1");
        player.experienceLevel-=3;
        return 1;
    }
}
