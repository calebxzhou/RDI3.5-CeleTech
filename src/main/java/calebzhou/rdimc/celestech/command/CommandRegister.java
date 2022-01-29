package calebzhou.rdimc.celestech.command;

import calebzhou.rdimc.celestech.command.impl.*;
import calebzhou.rdimc.celestech.command.impl.island.*;
import calebzhou.rdimc.celestech.command.impl.protect.SetProtectCommand;
import calebzhou.rdimc.celestech.event.RegisterCommandsCallback;
import net.minecraft.util.ActionResult;

import java.util.ArrayList;

public class CommandRegister {
    private final ArrayList<BaseCommand> commands = new ArrayList<>();
    public CommandRegister(){

        commands.add(new BiomeCommand("chunkbiome",0));
        commands.add(new CreateCommand("create",0));
        commands.add(new DeleteCommand("delete",0));
        commands.add(new DisconnectCommand("disconnect",0));
        commands.add(new HomeCommand("home",0));
        commands.add(new InviteCommand("invite",0));
        commands.add(new KickCommand("kick",0));
        commands.add(new LeapCommand("leap",0));
        commands.add(new LocateCommand("locate",0));
        commands.add(new QuitCommand("quit",0));
        commands.add(new SlowfallCommand("slowfall",0));
        commands.add(new SpawnCommand("spawn",0));
        commands.add(new SpawnCommand("back",0));
        commands.add(new TpaCommand("tpa",0));
        commands.add(new TpreqCommand("tpreq",0));
        commands.add(new TpsCommand("tps",0));
        commands.add(new PayExperienceCommand("pay",0));
        commands.add(new SetProtectCommand("protect",0));
        commands.add(new TrashCommand("trash",0));




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
