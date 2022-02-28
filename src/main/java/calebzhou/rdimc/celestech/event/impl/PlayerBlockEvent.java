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
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SaplingBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;

import java.util.Collection;

//实体与方块交互的相关事件
public class PlayerBlockEvent {
    public PlayerBlockEvent(){


        AttackBlockCallback.EVENT.register(((player, world, hand, pos, direction) -> {
            //如果玩家使用金锄头左键点击（选择区域点1）
            if(player.getMainHandStack().getItem() == Items.GOLDEN_HOE){
                handlePointSelection(player,pos,true);

            }
            return ActionResult.PASS;
        }));
        UseBlockCallback.EVENT.register(((player, world, hand, hitResult) -> {
            BlockPos blockPos = hitResult.getBlockPos();
            BlockState blockState = world.getBlockState(blockPos);
            String pid = player.getUuidAsString();
            //如果玩家使用金锄头右键点击（选择区域点2）
            if(player.getMainHandStack().getItem() == Items.GOLDEN_HOE){
                handlePointSelection(player,blockPos,false);

            }
            return ActionResult.PASS;
        }));
    }

    /**
     * @param left_right 左手点1 true 右手点2 false
     * */
    private void handlePointSelection(PlayerEntity player,BlockPos blockPos,boolean left_right){
        String pid = player.getUuidAsString();
        AreaSelection area = AreaSelection.map.get(pid);
        if(area==null)
            area = new AreaSelection();
        if(left_right){
            //左手
            area.setPos1(blockPos);
            TextUtils.sendChatMessage(player,ColorConstants.AQUA+"您成功选择了点1："+blockPos.toShortString());
        }else{
            //右手
            area.setPos2(blockPos);
            TextUtils.sendChatMessage(player, ColorConstants.BRIGHT_GREEN+"您成功选择了点2："+blockPos.toShortString());
        }

        AreaSelection.map.put(pid, area);
    }

}