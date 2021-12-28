package calebzhou.rdimc.celestech.event;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.BlockState;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;

public interface RegisterCommandsCallback {
    Event<RegisterCommandsCallback> EVENT = EventFactory.createArrayBacked(RegisterCommandsCallback.class,
            listeners -> (dispatcher,environment) ->{
                for(RegisterCommandsCallback listener:listeners){
                    ActionResult result = listener.register(dispatcher,environment);
                    if(result != ActionResult.PASS) {
                        return result;
                    }
                }
                return ActionResult.PASS;
            }
    );

    ActionResult register(CommandDispatcher<ServerCommandSource> dispatcher, CommandManager.RegistrationEnvironment environment);
}
