package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.RDICeleTech;
import calebzhou.rdimc.celestech.command.RdiCommand;
import calebzhou.rdimc.celestech.utils.TextUtils;
import calebzhou.rdimc.celestech.utils.ThreadPool;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

public class IpListCommand extends RdiCommand {
    public IpListCommand() {
        super("iplist");
    }


    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getExecution() {
        return baseArgBuilder.executes(exec->exec(exec.getSource().getPlayer()));
    }

    private int exec(ServerPlayer player) {
        if(player.experienceLevel<3)
            return 1;
        ThreadPool.newThread(()->{
            player.experienceLevel-=3;
            RDICeleTech.ipGeoMap.forEach((pname,geo)->{
                String msg;
                String[] split = geo.split(",");
                if(split.length==0)
                    msg="undefined";
                else if(split.length==1)
                    msg=split[0];
                else
                    msg=split[1];
                TextUtils.sendChatMessage(player,pname+" "+msg);
            });
        });

        return 1;
    }
}
