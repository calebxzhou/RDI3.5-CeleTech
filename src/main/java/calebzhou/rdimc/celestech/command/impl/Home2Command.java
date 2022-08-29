package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.RDICeleTech;
import calebzhou.rdimc.celestech.command.RdiCommand;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.model.Island2;
import calebzhou.rdimc.celestech.thread.RdiHttpPlayerRequest;
import calebzhou.rdimc.celestech.thread.RdiHttpRequest;
import calebzhou.rdimc.celestech.thread.RdiRequestThread;
import calebzhou.rdimc.celestech.utils.IslandUtils;
import calebzhou.rdimc.celestech.utils.PlayerUtils;
import calebzhou.rdimc.celestech.utils.RdiSerializer;
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
        super("home2");
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getExecution() {
        return baseArgBuilder.executes(context -> {
            ServerPlayer player = context.getSource().getPlayer();
            sendChatMessage(player,MessageType.INFO,"开始返回您的岛屿，请稍等...");
            RdiRequestThread.addTask(new RdiHttpPlayerRequest(
                    RdiHttpRequest.Type.get,
                    player,
                    resp->{
                        if(!resp.startsWith("{")){
                            sendChatMessage(player,MessageType.ERROR,resp);
                            return;
                        }
                        Island2 island2 = RdiSerializer.GSON.fromJson(resp, Island2.class);
                        ResourceLocation dim = IslandUtils.getIslandDimensionLoca(island2.iid);
                        ServerUtils.executeOnServerThread(()->{
                            RuntimeWorldHandle worldHandle = Fantasy.get(RDICeleTech.getServer()).getOrOpenPersistentWorld(dim, IslandUtils.getIslandWorldConfig());
                            ServerLevel world = worldHandle.asWorld();
                            PlayerUtils.addSlowFallEffect(player);
                            PlayerUtils.teleport(
                                    player,
                                    world,
                                    island2.loca.x,
                                    island2.loca.y+3,
                                    island2.loca.z,
                                    island2.loca.w,
                                    island2.loca.p);
                            sendChatMessage(player,MessageType.SUCCESS,"成功！");
                        });
                    },
                    "island2/" + player.getStringUUID()
            ));
            return 1;
        });
    }
}
