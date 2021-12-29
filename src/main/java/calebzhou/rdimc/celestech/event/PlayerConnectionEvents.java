package calebzhou.rdimc.celestech.event;

import calebzhou.rdimc.celestech.constant.LogAction;
import calebzhou.rdimc.celestech.model.record.LogInOutRecord;
import calebzhou.rdimc.celestech.utils.*;
import net.minecraft.util.ActionResult;

import static calebzhou.rdimc.celestech.constant.ServiceConstants.ADDR;

public class PlayerConnectionEvents {
    public PlayerConnectionEvents(){
        //连接服务器
        PlayerConnectServerCallback.EVENT.register(((connection, player) -> {
            HttpUtils.postObject(new LogInOutRecord(player, LogAction.IN));
            //发送天气预报
            ThreadPool.newThread(()-> {
                TextUtils.sendChatMessage(player, HttpUtils.doGet(ADDR+"getWeather?ip="+player.getIp()));
                TextUtils.sendChatMessage(player, TimeUtils.getTimeChineseString()+"好,"+player.getDisplayName().getString()+",欢迎回到RDI。");
                TextUtils.sendChatMessage(player,"创建空岛:/createisland 回到空岛:/island 邀请好友加入空岛");
                }
            );
            //返回自己空岛
            //ServerUtils.executeCommandOnSource("island",player.getCommandSource());
            //载入玩家路径监控
            ThreadPool.startPlayerThread(player);
            return ActionResult.PASS;
        }));
        //断开服务器
        PlayerDisconnectServerCallback.EVENT.register((player -> {
            HttpUtils.postObject(new LogInOutRecord(player, LogAction.OUT));
            ThreadPool.stopPlayerThread(player.getUuidAsString());
            return ActionResult.PASS;
        }));
    }
}
