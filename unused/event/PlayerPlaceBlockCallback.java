package calebzhou.rdimc.celestech.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

public interface PlayerPlaceBlockCallback {
    Event<PlayerPlaceBlockCallback> EVENT = EventFactory.createArrayBacked(PlayerPlaceBlockCallback.class,
            listeners -> (player,blockPos,blockState) ->{
                for(PlayerPlaceBlockCallback listener:listeners){
                    InteractionResult result = listener.interact(player,blockPos,blockState);
                    if(result != InteractionResult.PASS) {
                        return result;
                    }
                }
                return InteractionResult.PASS;
            }
    );

    InteractionResult interact(Player player, BlockPos blockPos, BlockState blockState);
}
