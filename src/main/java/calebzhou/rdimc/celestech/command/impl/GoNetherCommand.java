package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.RdiSharedConstants;
import calebzhou.rdimc.celestech.command.RdiCommand;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.utils.PlayerUtils;
import calebzhou.rdimc.celestech.utils.TextUtils;
import calebzhou.rdimc.celestech.utils.WorldUtils;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Blocks;

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
        String dimensionName = WorldUtils.getDimensionName(player.getLevel());
        if(!dimensionName.startsWith(RdiSharedConstants.ISLAND_DIMENSION_FULL_PREFIX)){
            TextUtils.sendChatMessage(player, MessageType.ERROR,"只有二岛能通过此功能前往地狱！");
            return 1;
        }
        if (player.experienceLevel<3) {
            TextUtils.sendChatMessage(player,MessageType.ERROR,"必须有3级经验才能前往地狱！");
            return 1;
        }
        Vec3i targetPos = WorldUtils.getIsland2ToNetherPos(WorldUtils.getIsland2IdInt(player.getLevel()));
        WorldUtils.placeBlock(WorldUtils.getNether(),new BlockPos(targetPos), Blocks.OBSIDIAN.defaultBlockState());
        PlayerUtils.teleport(player,WorldUtils.getNether() ,targetPos.getX(),targetPos.getY()+3,targetPos.getZ(),0,0);
        TextUtils.sendChatMessage(player,MessageType.SUCCESS,"1");
        player.experienceLevel-=3;
        return 1;
    }
}
