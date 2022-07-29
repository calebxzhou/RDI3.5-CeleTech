package calebzhou.rdimc.celestech;

import calebzhou.rdimc.celestech.command.BaseCommand;
import calebzhou.rdimc.celestech.command.impl.*;
import calebzhou.rdimc.celestech.command.impl.SpawnCommand;
import calebzhou.rdimc.celestech.command.impl.TpaCommand;
import calebzhou.rdimc.celestech.command.impl.TpreqCommand;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class CommandList {
    public static final ObjectArrayList<BaseCommand> commands = new ObjectArrayList<>();
    static{
        commands.add(new CreateCommand("start",0));
        commands.add(new DeleteCommand("confirm-delete",0));
        commands.add(new HomeCommand("home",0));
        commands.add(new InviteCommand("invite",0));
        commands.add(new KickCommand("kick",0));
        commands.add(new LocateCommand("loca",0));
        commands.add(new QuitCommand("quit",0));
        commands.add(new SlowfallCommand("slowfall",0));
        commands.add(new SpawnCommand("spawn",0));
        commands.add(new SpawnCommand("back",0));
        commands.add(new RollCommand("roll",0));
        commands.add(new TpaCommand("tpa",0));
        commands.add(new TpreqCommand("tpreq",0));
        commands.add(new TpsCommand("tps",0));
        commands.add(new PayExperienceCommand("pay",0));
    }
}
