package calebzhou.rdi.core.server.command.impl;

import calebzhou.rdi.core.server.RdiMemoryStorage;
import calebzhou.rdi.core.server.command.RdiCommand;
import calebzhou.rdi.core.server.utils.TextUtils;
import calebzhou.rdi.core.server.utils.ThreadPool;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

public class IpListCommand extends RdiCommand {
    public IpListCommand() {
        super("iplist");
    }


    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getExecution() {
        return baseArgBuilder.executes(this::exec);
    }

    private int exec(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        ServerPlayer player = source.getPlayer();
        if(player!=null){
            if(player.experienceLevel<3){
                PlayerUtils.sendChatMessage(player, PlayerUtils.RESPONSE_ERROR,"要有3级经验才能查询IP属地列表！");
                return 1;
            }else{
                player.experienceLevel-=3;
            }
        }
        ThreadPool.newThread(()->{
            PlayerUtils.sendChatMessage(player,"====ip属地列表====");
            StringBuilder ipinfo = new StringBuilder();
            RdiMemoryStorage.ipGeoMap.forEach((pname, geo)->{
                String msg;
                String[] split = geo.split(",");
                if(split.length==0)
                    msg="没有IP记录信息";
                else if(split.length==1)
                    msg=split[0];
                else
                    msg=split[1];
                ipinfo.append(pname).append(msg).append("    ");

            });
            PlayerUtils.sendChatMessage(player,ipinfo.toString());
        });


        return 1;
    }

}
