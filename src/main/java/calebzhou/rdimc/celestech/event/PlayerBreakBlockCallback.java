package calebzhou.rdimc.celestech.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.BlockState;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;

public interface PlayerBreakBlockCallback {
    Event<PlayerBreakBlockCallback> EVENT = EventFactory.createArrayBacked(PlayerBreakBlockCallback.class,
            listeners -> (player,blockPos,blockState) ->{
                for(PlayerBreakBlockCallback listener:listeners){
                    ActionResult result = listener.interact(player,blockPos,blockState);
                    if(result != ActionResult.PASS) {
                        return result;
                    }
                }
                return ActionResult.PASS;
            }
    );

    ActionResult interact(ServerPlayerEntity player, BlockPos blockPos, BlockState blockState);
}
