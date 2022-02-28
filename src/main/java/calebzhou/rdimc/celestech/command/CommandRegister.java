package calebzhou.rdimc.celestech.command;

import calebzhou.rdimc.celestech.command.impl.*;
import calebzhou.rdimc.celestech.module.protect.ProtectSetCommand;
import calebzhou.rdimc.celestech.event.RegisterCommandsCallback;
import calebzhou.rdimc.celestech.module.island.command.*;
import calebzhou.rdimc.celestech.module.structure.VirtualStructureCommand;
import calebzhou.rdimc.celestech.module.teleport.SpawnCommand;
import calebzhou.rdimc.celestech.module.teleport.TpaCommand;
import calebzhou.rdimc.celestech.module.teleport.TpreqCommand;
import net.minecraft.util.ActionResult;

import java.util.ArrayList;

public class CommandRegister {
    private final ArrayList<BaseCommand> commands = new ArrayList<>();
    public CommandRegister(){

        commands.add(new BiomeCommand("bio",0));
        commands.add(new CreateCommand("create",0));
        commands.add(new DeleteCommand("delete",0));
        commands.add(new ConfirmDeleteCommand("confirm-delete",0));
        commands.add(new HomeCommand("home",0));
        commands.add(new InviteCommand("invite",0));
        commands.add(new KickCommand("kick",0));
        commands.add(new IslandCommand("island",0));
        commands.add(new LeapCommand("leap",0));
        commands.add(new BindCommand("bind",0));
        commands.add(new LocateCommand("loca",0));
        commands.add(new QuitCommand("quit",0));
        commands.add(new ChatRangeCommand("chatrange",0));
        commands.add(new SlowfallCommand("slowfall",0));
        commands.add(new SpawnCommand("spawn",0));
        commands.add(new SpawnCommand("back",0));
        commands.add(new RollCommand("roll",0));
        commands.add(new TpaCommand("tpa",0));
        commands.add(new TpreqCommand("tpreq",0));
        commands.add(new TpsCommand("tps",0));
        commands.add(new PayExperienceCommand("pay",0));
        commands.add(new ProtectSetCommand("protect-set",0));
        commands.add(new TrashCommand("trash",0));
        commands.add(new VirtualStructureCommand("vs",0));
        //commands.add(new JoinCommand("join",0));




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
