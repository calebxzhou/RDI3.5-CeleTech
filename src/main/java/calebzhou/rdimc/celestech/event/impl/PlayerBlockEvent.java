package calebzhou.rdimc.celestech.event.impl;


import calebzhou.rdimc.celestech.RDICeleTech;
import calebzhou.rdimc.celestech.constant.ColorConstants;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.constant.WorldConstants;
import calebzhou.rdimc.celestech.event.PlayerBreakBlockCallback;
import calebzhou.rdimc.celestech.event.PlayerPlaceBlockCallback;
import calebzhou.rdimc.celestech.model.AreaSelection;
import calebzhou.rdimc.celestech.model.CoordLocation;
import calebzhou.rdimc.celestech.model.record.BlockRecord;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import calebzhou.rdimc.celestech.utils.TextUtils;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SaplingBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;

import java.util.Collection;

//实体与方块交互的相关事件
public class PlayerBlockEvent {
    public PlayerBlockEvent(){
        PlayerBreakBlockCallback.EVENT.register(((player, blockPos, blockState) -> {
            //如果玩家破坏了石头 不记录
            if(blockState.getBlock()== Blocks.STONE || blockState.getBlock()== Blocks.COBBLESTONE){
                return ActionResult.PASS;
            }
            record(player,blockPos,blockState, BlockRecord.Action.BREAK);

            return ActionResult.PASS;
        }));
        PlayerPlaceBlockCallback.EVENT.register(((player, blockPos, blockState) -> {
            record(player,blockPos,blockState, BlockRecord.Action.PLACE);
            return ActionResult.PASS;
        }));

        //右键单击方块事件
        UseBlockCallback.EVENT.register(((player, world, hand, hitResult) -> {
            BlockPos blockPos = hitResult.getBlockPos();
            BlockState blockState = world.getBlockState(blockPos);
            String pid = player.getUuidAsString();
            //如果是树苗，启动快速长树
            if(blockState.getBlock() instanceof SaplingBlock)
                new SaplingEvents(player, blockPos,blockState, ((SaplingBlock) blockState.getBlock()));
            //如果玩家使用金锄头右键点击（选择区域点）
            if(player.getMainHandStack().getItem() == Items.GOLDEN_HOE){
                Collection<BlockPos> clp = AreaSelection.map.get(pid);
                int points = clp.size();
                if(points>=2){
                    AreaSelection.map.removeAll(pid);
                    TextUtils.sendChatMessage(player,"您选择的区域点数量，超过了2个，请重新选择。", MessageType.ERROR);
                }else{
                    String msg = String.format("您成功选择了第%d个区域点，位置在%s", points+1, blockPos.toShortString());
                    if(points==0){
                        msg= ColorConstants.AQUA+msg;
                    }else if(points==1){
                        msg = ColorConstants.BRIGHT_GREEN+msg;
                    }
                    TextUtils.sendChatMessage(player,msg);
                    AreaSelection.map.put(pid, blockPos);
                }

            }
            return ActionResult.PASS;
        }));
    }
    private void record(Entity entity, BlockPos blockPos, BlockState blockState, BlockRecord.Action action){
        String dimension=entity.world.getDimension().getEffects().toString();
        //只记录主世界,如果不是主世界就不记录
        if(!dimension.equals(WorldConstants.DEFAULT_WORLD))
            return;
        int posX=blockPos.getX();
        int posY=blockPos.getY();
        int posZ=blockPos.getZ();
        if(posY==0 && posX==0 && posZ==0)
            return;
        CoordLocation location =new CoordLocation(dimension,posX,posY,posZ);
        String playerUuid=entity instanceof PlayerEntity ?entity.getUuidAsString() : entity.getEntityName();
        String blockType=blockState.getBlock().getTranslationKey();

        BlockRecord record=new BlockRecord(playerUuid,blockType,action,location);
        HttpUtils.asyncSendObject(record);
    }

}