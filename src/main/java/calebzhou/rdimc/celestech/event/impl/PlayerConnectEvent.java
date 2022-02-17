package calebzhou.rdimc.celestech.event.impl;

import calebzhou.rdimc.celestech.constant.ColorConstants;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.event.PlayerConnectServerCallback;
import calebzhou.rdimc.celestech.event.PlayerDisconnectServerCallback;
import calebzhou.rdimc.celestech.model.ApiResponse;
import calebzhou.rdimc.celestech.model.CoordLocation;
import calebzhou.rdimc.celestech.model.Island;
import calebzhou.rdimc.celestech.model.record.GenericRecord;
import calebzhou.rdimc.celestech.model.record.RecordType;
import calebzhou.rdimc.celestech.model.record.UuidNameRecord;
import calebzhou.rdimc.celestech.utils.*;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.text.MutableText;
import net.minecraft.util.ActionResult;

import java.util.Objects;

import static calebzhou.rdimc.celestech.utils.TextUtils.*;

public class PlayerConnectEvent {
    public PlayerConnectEvent(){
        //连接服务器
        PlayerConnectServerCallback.EVENT.register(((connection, player) -> {
            HttpUtils.asyncSendObject(new UuidNameRecord(player.getUuidAsString(), player.getEntityName()));
            HttpUtils.asyncSendObject(new GenericRecord(player.getUuidAsString(), RecordType.login, player.getIp(), null,null));
            //发送天气预报
            ThreadPool.newThread(()-> {
                TextUtils.sendChatMessage(player, HttpUtils.sendRequest("GET","api_v1_public/getWeather","ip="+player.getIp()));
                TextUtils.sendChatMessage(player, TimeUtils.getTimeChineseString()+"好,"+player.getDisplayName().getString());
                ApiResponse response = HttpUtils.sendRequestV2("GET","island/"+player.getUuidAsString(),"idType=pid");
                if(response==null || !response.isSuccess()){
                    sendChatMessage(player,"您还没有空岛呢。您可以：", MessageType.INFO);
                    MutableText comp = getClickableContentComp(ColorConstants.GOLD+"[创建岛屿]", "/create", " ");
                    MutableText comp1 = getClickableContentComp(ColorConstants.AQUA+"[加入朋友的岛屿]", "", "岛主输入/invite "+player.getEntityName()+"即可");
                    sendChatMessage(player,comp.append(comp1));
                }

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

}
