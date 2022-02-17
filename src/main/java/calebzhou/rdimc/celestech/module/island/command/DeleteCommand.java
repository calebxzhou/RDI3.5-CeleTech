package calebzhou.rdimc.celestech.module.island.command;

import calebzhou.rdimc.celestech.command.BaseCommand;
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

public class DeleteCommand extends BaseCommand {
    public DeleteCommand(String name, int permissionLevel) {
        super(name, permissionLevel,true);
    }
    protected void onExecute(ServerPlayerEntity player,String arg) {
        ApiResponse<Island> resp = HttpUtils.sendRequestV2("GET","v2/island/"+player.getUuidAsString());
        Island data;
        CoordLocation location = null;
        try {
            data = resp.getData(Island.class);
            location = CoordLocation.fromString(data.getLocation());
        } catch (NullPointerException e) {
            sendChatMessage(player,"您没有空岛!", MessageType.ERROR);
        }
        ApiResponse response = HttpUtils.sendRequestV2("DELETE","v2/island/"+player.getUuidAsString());
        if(!response.isSuccess()){
            sendChatMessage(player,response);
            return;
        }
        int offset=100;
        Vec3i v1 = new Vec3i(location.getPosiX() ,-64, location.getPosiZ());
        Vec3i v2= new Vec3i(location.getPosiX() ,320, location.getPosiZ());
        v1.add(-offset,0,-offset);
        v2.add(offset,0,offset);
        WorldUtils.fill(player.getWorld(), BlockBox.create(v1,v2), Blocks.AIR.getDefaultState());
        player.getInventory().clear();
        player.kill();
        PlayerUtils.teleport(player, WorldConstants.SPAWN_LOCA);
        player.setSpawnPoint(World.OVERWORLD,new BlockPos(WorldConstants.SPAWN_LOCA.getPosiX(), WorldConstants.SPAWN_LOCA.getPosiY(), WorldConstants.SPAWN_LOCA.getPosiZ()),0,true,false);
        sendChatMessage(player,response);
    }

}
