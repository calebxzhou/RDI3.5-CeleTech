package calebzhou.rdimc.celestech;

import calebzhou.rdimc.celestech.command.BaseCommand;
import calebzhou.rdimc.celestech.command.impl.*;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class CommandList {
    public static final ObjectArrayList<BaseCommand> commands = new ObjectArrayList<>();
    static{
        commands.add(new CreateCommand("create",0));
        commands.add(new DeleteCommand("999delete",0));
        commands.add(new HomeCommand("home",0));
        commands.add(new InviteCommand("invite",0));
        commands.add(new KickCommand("kickout",0));
        commands.add(new LocaCommand("loca",0));
        commands.add(new SlowfallCommand("slowfall",0));
        commands.add(new SpawnCommand("spawn",0));
        commands.add(new SpawnCommand("back",0));
        commands.add(new TpaCommand("tpa",0));
        commands.add(new TpreqCommand("tpreq",0));
        commands.add(new TpsCommand("tps",0));
    }
}
