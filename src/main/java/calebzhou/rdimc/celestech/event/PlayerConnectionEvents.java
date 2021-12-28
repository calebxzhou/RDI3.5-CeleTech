package calebzhou.rdimc.celestech.event;

import calebzhou.rdimc.celestech.utils.HttpUtils;
import calebzhou.rdimc.celestech.utils.ServerUtils;
import calebzhou.rdimc.celestech.utils.TextUtils;
import calebzhou.rdimc.celestech.utils.ThreadPool;
import net.minecraft.util.ActionResult;

import static calebzhou.rdimc.celestech.constant.ServiceConstants.ADDR;

public class PlayerConnectionEvents {
    public PlayerConnectionEvents(){
        PlayerConnectServerCallback.EVENT.register(((connection, player) -> {
            //发送天气预报
            ThreadPool.newThread(()-> TextUtils.sendChatMessage(player, HttpUtils.doGet(ADDR+"getWeather?ip="+player.getIp())));
            //返回自己空岛
            ServerUtils.executeCommandOnSource("island",player.getCommandSource());
            return ActionResult.PASS;
        }));
    }
}
