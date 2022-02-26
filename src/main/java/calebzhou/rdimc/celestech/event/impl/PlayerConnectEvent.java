package calebzhou.rdimc.celestech.event.impl;

import calebzhou.rdimc.celestech.event.PlayerConnectServerCallback;
import calebzhou.rdimc.celestech.event.PlayerDisconnectServerCallback;
import calebzhou.rdimc.celestech.model.ApiResponse;
import calebzhou.rdimc.celestech.model.Island;
import calebzhou.rdimc.celestech.model.record.GenericRecord;
import calebzhou.rdimc.celestech.model.record.RecordType;
import calebzhou.rdimc.celestech.model.record.UuidNameRecord;
import calebzhou.rdimc.celestech.module.island.IslandException;
import calebzhou.rdimc.celestech.utils.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;

public class PlayerConnectEvent {
    public PlayerConnectEvent(){
        //连接服务器
        PlayerConnectServerCallback.EVENT.register(((connection, player) -> {
            HttpUtils.asyncSendObject(new UuidNameRecord(player.getUuidAsString(), player.getEntityName()));
            HttpUtils.asyncSendObject(new GenericRecord(player.getUuidAsString(), RecordType.login, player.getIp(), null,null));
            //发送天气预报
            ThreadPool.newThread(()-> {
                sendWeather(player);
                checkHasIsland(player);



            }
            );


           // PlayerUtils.teleport(player, new CoordLocation(0,138,0));
            //载入玩家路径监控
          //  ThreadPool.startPlayerThread(player);
            return ActionResult.PASS;
        }));
        //断开服务器
        PlayerDisconnectServerCallback.EVENT.register((player -> {
            HttpUtils.asyncSendObject(new GenericRecord(player.getUuidAsString(), RecordType.logout, null, null,null));
          //  ThreadPool.stopPlayerThread(player);
            ServerUtils.save();
            return ActionResult.PASS;
        }));
    }
    private void sendWeather(ServerPlayerEntity player){
        TextUtils.sendChatMessage(player, HttpUtils.sendRequest("GET","api_v1_public/getWeather","ip="+player.getIp()));
        TextUtils.sendChatMessage(player, TimeUtils.getTimeChineseString()+"好,"+player.getDisplayName().getString());
    }
    private void checkHasIsland (ServerPlayerEntity player){
        ApiResponse<Island> response = null;
        boolean hasIsland = true;
        try {
            response = HttpUtils.sendRequestV2("GET","v2/island/"+player.getUuidAsString());
            //player.networkHandler.sendPacket(new IslandInfoS2CPacket(response.getData(Island.class)));
        } catch (NullPointerException| IslandException e) {
            hasIsland=false;
        }
        if(response==null || !response.isSuccess() || !hasIsland){
            NetworkUtils.sendPacketS2C(player,NetworkUtils.ISLAND_INFO,"404");
            /*sendChatMessage(player,"您还没有空岛呢。您可以：", MessageType.INFO);
            MutableText comp = getClickableContentComp(ColorConstants.GOLD+"[创建岛屿]", "/create", " ");
            MutableText comp1 = getClickableContentComp(ColorConstants.AQUA+"[加入朋友的岛屿]", "", "岛主输入/invite "+player.getEntityName()+"即可");
            sendChatMessage(player,comp.append(comp1));*/
        }
    }

}
