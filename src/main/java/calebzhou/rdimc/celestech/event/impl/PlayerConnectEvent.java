package calebzhou.rdimc.celestech.event.impl;

import calebzhou.rdimc.celestech.event.PlayerConnectServerCallback;
import calebzhou.rdimc.celestech.event.PlayerDisconnectServerCallback;
import calebzhou.rdimc.celestech.model.cache.BaseServerCache;
import calebzhou.rdimc.celestech.model.cache.ChatRecordCache;
import calebzhou.rdimc.celestech.model.record.GenericRecord;
import calebzhou.rdimc.celestech.model.record.RecordType;
import calebzhou.rdimc.celestech.model.record.UuidNameRecord;
import calebzhou.rdimc.celestech.utils.*;
import com.google.gson.Gson;
import net.minecraft.util.ActionResult;

import java.util.ArrayList;

import static calebzhou.rdimc.celestech.constant.ServiceConstants.ADDR;

public class PlayerConnectEvent {
    public PlayerConnectEvent(){
        //连接服务器
        PlayerConnectServerCallback.EVENT.register(((connection, player) -> {
            HttpUtils.postObject(new UuidNameRecord(player.getUuidAsString(), player.getEntityName()));
            HttpUtils.postObject(new GenericRecord(player.getUuidAsString(), RecordType.login, player.getIp(), null,null));
            //发送天气预报
            ThreadPool.newThread(()-> {
                TextUtils.sendChatMessage(player, HttpUtils.doGet(ADDR+"getWeather?ip="+player.getIp()));
                TextUtils.sendChatMessage(player, TimeUtils.getTimeChineseString()+"好,"+player.getDisplayName().getString()+",欢迎回到RDI。");
                //载入聊天缓存
                if(!ChatRecordCache.instance.isFull()){
                    String json=HttpUtils.get("GenericRecord","query=SELECT * FROM GenericRecord where recordType='chat' order by recTime desc limit 48");
                    ArrayList<GenericRecord> list = new Gson().fromJson(json, ArrayList.class);
                    list.stream().forEach(e->{
                        ChatRecordCache.instance.put(e.getSrc(),e);
                    });
                }
                BaseServerCache.chatRecord.forEach((k, v)->{
                    TextUtils.sendChatMessage(player,
                            String.format("%s %s:%s",
                                    TimeUtils.getComparedDateTime(v.getRecTime()),
                                    k,
                                    v.getContent()));
                });
            }
            );

            //载入玩家路径监控
            ThreadPool.startPlayerThread(player);
            return ActionResult.PASS;
        }));
        //断开服务器
        PlayerDisconnectServerCallback.EVENT.register((player -> {
            HttpUtils.postObject(new GenericRecord(player.getUuidAsString(), RecordType.logout, null, null,null));
            ThreadPool.stopPlayerThread(player);
            ServerUtils.save();
            return ActionResult.PASS;
        }));
    }
}
