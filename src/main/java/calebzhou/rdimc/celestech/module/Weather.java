package calebzhou.rdimc.celestech.module;

import calebzhou.rdimc.celestech.api.CallbackRegisterable;
import calebzhou.rdimc.celestech.event.PlayerConnectServerCallback;
import calebzhou.rdimc.celestech.utils.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;

public class Weather implements CallbackRegisterable {

    public Weather() {

    }

    public void sendWeather(ServerPlayer player){
        TextUtils.sendChatMessage(player, HttpUtils.sendRequest("GET","api_v1_public/getWeather","ip="+player.getIpAddress()));
        TextUtils.sendChatMessage(player, TimeUtils.getTimeChineseString()+"好,"+player.getDisplayName().getString());
    }

    @Override
    public void registerCallbacks() {
        PlayerConnectServerCallback.EVENT.register(IdentifierUtils.byClass(this.getClass()),((connection, player) -> {
            ThreadPool.newThread(()->sendWeather(player));
            return InteractionResult.PASS;
        }));
    }
}
