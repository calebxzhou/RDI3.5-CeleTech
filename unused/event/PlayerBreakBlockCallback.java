package calebzhou.rdi.celestech.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.block.state.BlockState;

public interface PlayerBreakBlockCallback {
    Event<PlayerBreakBlockCallback> EVENT = EventFactory.createArrayBacked(PlayerBreakBlockCallback.class,
            listeners -> (player,blockPos,blockState) ->{
                for(PlayerBreakBlockCallback listener:listeners){
                    InteractionResult result = listener.interact(player,blockPos,blockState);
                    if(result != InteractionResult.PASS) {
                        return result;
                    }
                }
                return InteractionResult.PASS;
            }
    );

    InteractionResult interact(ServerPlayer player, BlockPos blockPos, BlockState blockState);
}
