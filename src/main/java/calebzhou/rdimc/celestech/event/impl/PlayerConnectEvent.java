package calebzhou.rdimc.celestech.event.impl;

import calebzhou.rdimc.celestech.event.PlayerConnectServerCallback;
import calebzhou.rdimc.celestech.event.PlayerDisconnectServerCallback;
import calebzhou.rdimc.celestech.model.CoordLocation;
import calebzhou.rdimc.celestech.model.record.GenericRecord;
import calebzhou.rdimc.celestech.model.record.RecordType;
import calebzhou.rdimc.celestech.model.record.UuidNameRecord;
import calebzhou.rdimc.celestech.utils.*;
import net.minecraft.util.ActionResult;

public class PlayerConnectEvent {
    public PlayerConnectEvent(){
        //连接服务器
        PlayerConnectServerCallback.EVENT.register(((connection, player) -> {
            HttpUtils.asyncSendObject(new UuidNameRecord(player.getUuidAsString(), player.getEntityName()));
            HttpUtils.asyncSendObject(new GenericRecord(player.getUuidAsString(), RecordType.login, player.getIp(), null,null));
            //发送天气预报
            ThreadPool.newThread(()-> {
                       /* ChatRecordCache.instance.loadCache();
                        ChatRecordCache.instance.getRecordList().forEach(e -> TextUtils.sendChatMessage(player,
                                String.format("%s %s:  %s",
                                        TimeUtils.getComparedDateTime(e.getRecTime()),
                                        e.getSrc(),
                                        e.getContent())));*/
                TextUtils.sendChatMessage(player, HttpUtils.sendRequest("GET","api_v1_public/getWeather","ip="+player.getIp()));
                TextUtils.sendChatMessage(player, TimeUtils.getTimeChineseString()+"好,"+player.getDisplayName().getString()+",欢迎回到RDI。");
                //载入聊天缓存

            }
            );


           // PlayerUtils.teleport(player, new CoordLocation(0,138,0));
            //载入玩家路径监控
            ThreadPool.startPlayerThread(player);
            return ActionResult.PASS;
        }));
        //断开服务器
        PlayerDisconnectServerCallback.EVENT.register((player -> {
            HttpUtils.asyncSendObject(new GenericRecord(player.getUuidAsString(), RecordType.logout, null, null,null));
            ThreadPool.stopPlayerThread(player);
            ServerUtils.save();
            return ActionResult.PASS;
        }));
    }

}
