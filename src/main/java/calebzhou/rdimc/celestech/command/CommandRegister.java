package calebzhou.rdimc.celestech.command;

import calebzhou.rdimc.celestech.command.impl.DeleteIslandCommand;
import calebzhou.rdimc.celestech.command.impl.InventoryCommand;
import calebzhou.rdimc.celestech.command.impl.IslandCommand;
import calebzhou.rdimc.celestech.event.RegisterCommandsCallback;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.ActionResult;

import java.util.ArrayList;

public class CommandRegister {
    private final ArrayList<BaseCommand> commands = new ArrayList<>();
    public CommandRegister(){


        commands.add(new IslandCommand("island",0));
        commands.add(new InventoryCommand("inv",0));
        commands.add(new DeleteIslandCommand("deleteisland",0));


        RegisterCommandsCallback.EVENT.register(((dispatcher, environment) -> {
            commands.forEach((cmd) -> {
                if (cmd.setExecution() != null) {
                    dispatcher.register(cmd.getBuilder());
                }
            });
            return ActionResult.PASS;
        }));
    }
}
