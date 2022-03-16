package calebzhou.rdimc.celestech.event;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.world.InteractionResult;

public interface RegisterCommandsCallback {
    Event<RegisterCommandsCallback> EVENT = EventFactory.createArrayBacked(RegisterCommandsCallback.class,
            listeners -> (dispatcher,environment) ->{
                for(RegisterCommandsCallback listener:listeners){
                    InteractionResult result = listener.register(dispatcher,environment);
                    if(result != InteractionResult.PASS) {
                        return result;
                    }
                }
                return InteractionResult.PASS;
            }
    );

    InteractionResult register(CommandDispatcher<CommandSourceStack> dispatcher, Commands.CommandSelection environment);
}
