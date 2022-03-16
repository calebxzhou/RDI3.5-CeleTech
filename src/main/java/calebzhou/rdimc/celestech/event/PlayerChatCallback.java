package calebzhou.rdimc.celestech.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.TextFilter;
import net.minecraft.world.InteractionResult;

public interface PlayerChatCallback {
    Event<PlayerChatCallback> EVENT = EventFactory.createArrayBacked(PlayerChatCallback.class,
            listeners -> (player,message) ->{
                for(PlayerChatCallback listener:listeners){
                    InteractionResult result = listener.call(player,message);
                    if(result != InteractionResult.PASS) {
                        return result;
                    }
                }
                return InteractionResult.PASS;
            }
    );

    InteractionResult call(ServerPlayer player, TextFilter.FilteredText message);
}
