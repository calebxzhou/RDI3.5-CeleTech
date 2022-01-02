package calebzhou.rdimc.celestech.command;

import calebzhou.rdimc.celestech.command.impl.*;
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
        commands.add(new TpaCommand("tpa",0));
        commands.add(new TpyesCommand("tpyes",0));
        commands.add(new TpsCommand("tps",0));


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
