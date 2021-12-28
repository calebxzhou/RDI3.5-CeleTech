package calebzhou.rdimc.celestech.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;

public interface PlayerConnectServerCallback {
    Event<PlayerConnectServerCallback> EVENT = EventFactory.createArrayBacked(PlayerConnectServerCallback.class,
            listeners -> (connection,player) ->{
                for(PlayerConnectServerCallback listener:listeners){
                    ActionResult result = listener.connect(connection,player);
                    if(result != ActionResult.PASS) {
                        return result;
                    }
                }
                return ActionResult.PASS;
            }
    );

    ActionResult connect(ClientConnection connection, ServerPlayerEntity player);
}
