package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.RDICeleTech;
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
import net.minecraft.server.level.ServerLevel;
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
        int islandId;
        try {
            islandId = Integer.parseInt(dimensionName.replace(RdiSharedConstants.ISLAND_DIMENSION_FULL_PREFIX,""));
        } catch (NumberFormatException e) {
            TextUtils.sendChatMessage(player,MessageType.ERROR,"维度数字格式错误！");
            return 1;
        }
        int netherRatioX=40;
        int netherRatioZ=40;
        //0=一象限 3=四象限
        int quadrant = islandId % 4;
        switch (quadrant){
            case 1-> {
                netherRatioX *= -1;
            }
            case 2-> {
                netherRatioX *= -1;
                netherRatioZ *= -1;
            }
            case 3-> {
                netherRatioZ *= -1;
            }
        }

        int netherTargetX = islandId * netherRatioX;
        int netherTargetZ = islandId * netherRatioZ;
        final int netherTargetY = 96;
        WorldUtils.placeBlock(WorldUtils.getNether(),new BlockPos(netherTargetX,netherTargetY,netherTargetZ), Blocks.OBSIDIAN.defaultBlockState());
        PlayerUtils.teleport(player,WorldUtils.getNether() ,netherTargetX,netherTargetY,netherTargetZ,0,0);
        return 1;
    }
}
