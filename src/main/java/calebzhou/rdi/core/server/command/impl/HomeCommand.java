package calebzhou.rdi.core.server.command.impl;

import calebzhou.rdi.core.server.RdiCoreServer;
import calebzhou.rdi.core.server.command.RdiCommand;
import calebzhou.rdi.core.server.model.Island2;
import calebzhou.rdi.core.server.thread.RdiHttpPlayerRequest;
import calebzhou.rdi.core.server.thread.RdiHttpRequest;
import calebzhou.rdi.core.server.thread.RdiRequestThread;
import calebzhou.rdi.core.server.utils.IslandUtils;
import calebzhou.rdi.core.server.utils.PlayerUtils;
import calebzhou.rdi.core.server.utils.RdiSerializer;
import calebzhou.rdi.core.server.utils.ServerUtils;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import xyz.nucleoid.fantasy.Fantasy;
import xyz.nucleoid.fantasy.RuntimeWorldHandle;

import static calebzhou.rdi.core.server.utils.TextUtils.sendChatMessage;

public class HomeCommand extends RdiCommand {
    public HomeCommand() {
        super("home");
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getExecution() {
        return baseArgBuilder.executes(context -> {
            ServerPlayer player = context.getSource().getPlayer();
            sendChatMessage(player, PlayerUtils.RESPONSE_INFO,"开始返回您的岛屿，请稍等...");
            RdiRequestThread.addTask(new RdiHttpPlayerRequest(
                    RdiHttpRequest.Type.get,
                    player,
                    resp->{
                        if(!resp.startsWith("{")){
                            sendChatMessage(player,PlayerUtils.RESPONSE_ERROR,resp);
                            return;
                        }
                        Island2 island2 = RdiSerializer.GSON.fromJson(resp, Island2.class);
                        ResourceLocation dim = IslandUtils.getIslandDimensionLoca(island2.iid);
                        ServerUtils.executeOnServerThread(()->{
                            RuntimeWorldHandle worldHandle = Fantasy.get(RdiCoreServer.getServer()).getOrOpenPersistentWorld(dim, IslandUtils.getIslandWorldConfig());
                            ServerLevel world = worldHandle.asWorld();
                            PlayerUtils.teleport(
                                    player,
                                    world,
                                    island2.loca.x,
                                    island2.loca.y+3,
                                    island2.loca.z,
                                    island2.loca.w,
                                    island2.loca.p);
                            sendChatMessage(player,PlayerUtils.RESPONSE_SUCCESS,"成功！");
                        });
                    },
                    "island2/" + player.getStringUUID()
            ));
            return 1;
        });
    }
}
