package calebzhou.rdimc.celestech.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;

public interface PlayerConnectServerCallback {
    Event<PlayerConnectServerCallback> EVENT = EventFactory.createArrayBacked(PlayerConnectServerCallback.class,
            listeners -> (connection,player) ->{
                for(PlayerConnectServerCallback listener:listeners){
                    InteractionResult result = listener.connect(connection,player);
                    if(result != InteractionResult.PASS) {
                        return result;
                    }
                }
                return InteractionResult.PASS;
            }
    );

    InteractionResult connect(Connection connection, ServerPlayer player);
}
