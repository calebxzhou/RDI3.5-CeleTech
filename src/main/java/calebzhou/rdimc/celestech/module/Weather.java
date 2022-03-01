package calebzhou.rdimc.celestech.module;

import calebzhou.rdimc.celestech.api.CallbackRegisterable;
import calebzhou.rdimc.celestech.event.PlayerConnectServerCallback;
import calebzhou.rdimc.celestech.utils.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;

public class Weather implements CallbackRegisterable {

    public Weather() {

    }

    public void sendWeather(ServerPlayerEntity player){
        TextUtils.sendChatMessage(player, HttpUtils.sendRequest("GET","api_v1_public/getWeather","ip="+player.getIp()));
        TextUtils.sendChatMessage(player, TimeUtils.getTimeChineseString()+"好,"+player.getDisplayName().getString());
    }

    @Override
    public void registerCallbacks() {
        PlayerConnectServerCallback.EVENT.register(IdentifierUtils.byClass(this.getClass()),((connection, player) -> {
            ThreadPool.newThread(()->sendWeather(player));
            return ActionResult.PASS;
        }));
    }
}
