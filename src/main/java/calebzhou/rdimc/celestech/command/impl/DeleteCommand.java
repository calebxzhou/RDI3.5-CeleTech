package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.command.BaseCommand;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.constant.WorldConst;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import calebzhou.rdimc.celestech.utils.PlayerUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

import static calebzhou.rdimc.celestech.utils.TextUtils.sendChatMessage;

public class DeleteCommand extends BaseCommand {
    public DeleteCommand(String name, int permissionLevel) {
        super(name, permissionLevel, true);
    }

    protected void onExecute(ServerPlayer player,String arg) {
        String resp = HttpUtils.sendRequest("delete", "island/" + player.getStringUUID());
        if(resp.equals("1")){
            player.getInventory().clearContent();
            player.kill();
            PlayerUtils.teleport(player, WorldConst.SPAWN_LOCA);
            player.setRespawnPosition(Level.OVERWORLD,new BlockPos(WorldConst.SPAWN_LOCA.x, WorldConst.SPAWN_LOCA.y, WorldConst.SPAWN_LOCA.z),0,true,false);
            sendChatMessage(player, MessageType.SUCCESS,"1");
        }else{
            sendChatMessage(player, MessageType.ERROR,"您未拥有空岛！");
        }
        /*int offset=100;
        Vec3i v1 = new Vec3i(location.getPosiX() ,-64, location.getPosiZ());
        Vec3i v2= new Vec3i(location.getPosiX() ,320, location.getPosiZ());
        v1.add(-offset,0,-offset);
        v2.add(offset,0,offset);
        WorldUtils.fill(player.getWorld(), BlockBox.create(v1,v2), Blocks.AIR.getDefaultState());*/
    }
}
