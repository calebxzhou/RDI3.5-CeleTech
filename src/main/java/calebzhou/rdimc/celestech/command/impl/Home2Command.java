package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.RDICeleTech;
import calebzhou.rdimc.celestech.command.RdiCommand;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.thread.RdiHttpPlayerRequest;
import calebzhou.rdimc.celestech.thread.RdiHttpRequest;
import calebzhou.rdimc.celestech.thread.RdiIslandRequestThread;
import calebzhou.rdimc.celestech.utils.PlayerUtils;
import calebzhou.rdimc.celestech.utils.ServerUtils;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import xyz.nucleoid.fantasy.Fantasy;
import xyz.nucleoid.fantasy.RuntimeWorldHandle;

import static calebzhou.rdimc.celestech.utils.TextUtils.sendChatMessage;

public class Home2Command extends RdiCommand {
    public Home2Command() {
        super("home");
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getExecution() {
        return baseArgBuilder.executes(context -> {

            ServerPlayer player = context.getSource().getPlayer();
            sendChatMessage(player,MessageType.INFO,"开始返回您的岛屿，请稍等...");
            RdiIslandRequestThread.addTask(new RdiHttpPlayerRequest(
                    RdiHttpRequest.Type.get,
                    player,
                    resp->{
                        if(resp.equals("0")){
                            sendChatMessage(player, MessageType.ERROR,"您没加入任何一岛屿！");
                            return;
                        }
                        String[] split = resp.split(",");
                        String iid = split[0];
                        double x= Double.parseDouble(split[1]);
                        double y= Double.parseDouble(split[2]);
                        double z= Double.parseDouble(split[3]);
                        double w= Double.parseDouble(split[4]);
                        double p= Double.parseDouble(split[5]);
                        ResourceLocation dim = Island2Command.getIslandDimensionLoca(iid);
                        ServerUtils.executeOnServerThread(()->{
                            RuntimeWorldHandle worldHandle = Fantasy.get(RDICeleTech.getServer()).getOrOpenPersistentWorld(dim, Island2Command.getIslandWorldConfig());
                            ServerLevel world = worldHandle.asWorld();
                            PlayerUtils.addSlowFallEffect(player);
                            PlayerUtils.teleport(player,world,x,y+3,z,w,p);
                            sendChatMessage(player,MessageType.SUCCESS,"成功！");
                        });
                    },
                    "island2/" + player.getStringUUID()
            ));
            return 1;
        });
    }
}
