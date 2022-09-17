package calebzhou.rdi.celestech.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;

public interface PlayerDeathCallback {
    Event<PlayerDeathCallback> EVENT = EventFactory.createArrayBacked(PlayerDeathCallback.class,
            listeners -> (player,source) ->{
                for(PlayerDeathCallback listener:listeners){
                    InteractionResult result = listener.call(player,source);
                    if(result != InteractionResult.PASS) {
                        return result;
                    }
                }
                return InteractionResult.PASS;
            }
    );

    InteractionResult call(Player player, DamageSource source);
}
