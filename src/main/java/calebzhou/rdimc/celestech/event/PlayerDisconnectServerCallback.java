package calebzhou.rdimc.celestech.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;

public interface PlayerDisconnectServerCallback {
    Event<PlayerDisconnectServerCallback> EVENT = EventFactory.createArrayBacked(PlayerDisconnectServerCallback.class,
            listeners -> (player) ->{
                for(PlayerDisconnectServerCallback listener:listeners){
                    ActionResult result = listener.connect(player);
                    if(result != ActionResult.PASS) {
                        return result;
                    }
                }
                return ActionResult.PASS;
            }
    );

    ActionResult connect( ServerPlayerEntity player);
}
