package calebzhou.rdimc.celestech.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;

public interface PlayerDisconnectServerCallback {
    Event<PlayerDisconnectServerCallback> EVENT = EventFactory.createArrayBacked(PlayerDisconnectServerCallback.class,
            listeners -> (player) ->{
                for(PlayerDisconnectServerCallback listener:listeners){
                    InteractionResult result = listener.connect(player);
                    if(result != InteractionResult.PASS) {
                        return result;
                    }
                }
                return InteractionResult.PASS;
            }
    );

    InteractionResult connect( ServerPlayer player);
}
