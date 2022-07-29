package calebzhou.rdimc.celestech.module;

import calebzhou.rdimc.celestech.api.CallbackRegisterable;
import calebzhou.rdimc.celestech.event.PlayerConnectServerCallback;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;

public class Weather implements CallbackRegisterable {

    public Weather() {

    }

    public void sendWeather(ServerPlayer player){
        }

    @Override
    public void registerCallbacks() {
        PlayerConnectServerCallback.EVENT.register(IdentifierUtils.byClass(this.getClass()),((connection, player) -> {

            return InteractionResult.PASS;
        }));
    }
}
