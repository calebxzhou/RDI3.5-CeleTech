package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.command.NoArgCommand;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.constant.WorldConstants;
import calebzhou.rdimc.celestech.model.ApiResponse;
import calebzhou.rdimc.celestech.model.CoordLocation;
import calebzhou.rdimc.celestech.model.Island;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import calebzhou.rdimc.celestech.utils.PlayerUtils;
import calebzhou.rdimc.celestech.utils.WorldUtils;
import net.minecraft.block.Blocks;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import static calebzhou.rdimc.celestech.utils.TextUtils.sendChatMessage;

public class DeleteCommand extends NoArgCommand {
    public DeleteCommand(String name, int permissionLevel) {
        super(name, permissionLevel,true);
    }
    @Override
    protected void onExecute(ServerPlayerEntity player) {
        ApiResponse<Island> resp = HttpUtils.sendRequest("GET","island/"+player.getUuidAsString(),"idType=pid");
        Island data;CoordLocation location = null;
        try {
            data = resp.getData(Island.class);
            location = CoordLocation.fromString(data.getLocation());
        } catch (NullPointerException e) {
            sendChatMessage(player,"您没有空岛!", MessageType.ERROR);
        }
        ApiResponse response = HttpUtils.sendRequest("DELETE","island/"+player.getUuidAsString());
        if(!response.isSuccess()){
            sendChatMessage(player,response);
            return;
        }
        int offset=50;
        Vec3i v1 = new Vec3i(location.getPosiX() ,location.getPosiY(), location.getPosiZ());
        Vec3i v2= v1;
        v1.add(-offset,-offset,-offset);
        v2.add(offset,offset,offset);
        WorldUtils.fill(player.getWorld(), BlockBox.create(v1,v2), Blocks.AIR.getDefaultState());
        player.getInventory().clear();
        player.kill();
        PlayerUtils.teleport(player, WorldConstants.SPAWN_LOCA);
        player.setSpawnPoint(World.OVERWORLD,new BlockPos(WorldConstants.SPAWN_LOCA.getPosiX(), WorldConstants.SPAWN_LOCA.getPosiY(), WorldConstants.SPAWN_LOCA.getPosiZ()),0,true,false);
        sendChatMessage(player,response);
    }

}
