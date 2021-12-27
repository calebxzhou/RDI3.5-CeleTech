package calebzhou.rdimc.celestech.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;

public interface PlayerPlaceBlockCallback {
    Event<PlayerPlaceBlockCallback> EVENT = EventFactory.createArrayBacked(PlayerPlaceBlockCallback.class,
            listeners -> (player,blockPos,blockState) ->{
                for(PlayerPlaceBlockCallback listener:listeners){
                    ActionResult result = listener.interact(player,blockPos,blockState);
                    if(result != ActionResult.PASS) {
                        return result;
                    }
                }
                return ActionResult.PASS;
            }
    );

    ActionResult interact(PlayerEntity player, BlockPos blockPos, BlockState blockState);
}
