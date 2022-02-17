package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.command.BaseCommand;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.model.PlayerLocation;
import calebzhou.rdimc.celestech.utils.PlayerUtils;
import net.minecraft.block.Blocks;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

import static calebzhou.rdimc.celestech.utils.TextUtils.*;

public class LeapCommand extends BaseCommand {
    public LeapCommand(String name, int permissionLevel) {
        super(name, permissionLevel,false);
    }

    @Override
    protected void onExecute(ServerPlayerEntity fromPlayer,String arg) {
        BlockPos lookingAtBlock = PlayerUtils.getPlayerLookingAtBlock(fromPlayer, false);
        if(lookingAtBlock==null){
            sendChatMessage(fromPlayer,"您需要瞄准64格以内的方块以传送。", MessageType.ERROR);
            return;
        }
        if(fromPlayer.getWorld().getBlockState(lookingAtBlock).getBlock() == Blocks.AIR){
            sendChatMessage(fromPlayer,"您需要瞄准64格以内的方块以传送。", MessageType.ERROR);
            return;
        }
        if(!PlayerUtils.checkExpLevel(fromPlayer,1)){
            sendChatMessage(fromPlayer,"您的经验不足，本操作需要1级经验。",MessageType.ERROR);
            return;
        }
        if(!PlayerUtils.isOverworld(fromPlayer)){
            sendChatMessage(fromPlayer,"此世界引力过强，无法使用本指令" , MessageType.ERROR);
            return;
        }
        PlayerUtils.teleport(fromPlayer, PlayerLocation.fromBlockPos(lookingAtBlock.add(0,2,0),PlayerUtils.getDimensionName(fromPlayer), fromPlayer.getYaw(), fromPlayer.getPitch()));
        sendChatMessage(fromPlayer,"传送成功！",MessageType.SUCCESS);
    }
}
