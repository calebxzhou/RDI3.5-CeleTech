package calebzhou.rdi.core.server.command.impl;

import calebzhou.rdi.core.server.RdiCoreServer;
import calebzhou.rdi.core.server.command.RdiCommand;
import calebzhou.rdi.core.server.model.Island2;
import calebzhou.rdi.core.server.model.ResultData;
import calebzhou.rdi.core.server.utils.*;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import xyz.nucleoid.fantasy.Fantasy;
import xyz.nucleoid.fantasy.RuntimeWorldHandle;

import static calebzhou.rdi.core.server.utils.PlayerUtils.RESPONSE_ERROR;
import static calebzhou.rdi.core.server.utils.PlayerUtils.sendChatMessage;

public class HomeCommand extends RdiCommand {
	public HomeCommand() {
        super("home","回到我的岛屿");
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getExecution() {
        return baseArgBuilder.executes(context -> {
            ServerPlayer player = context.getSource().getPlayer();
            sendChatMessage(player, PlayerUtils.RESPONSE_INFO,"开始返回您的岛屿，请稍等...");
			ThreadPool.newThread(()->{
				ResultData<Island2> resultData = IslandUtils.getIslandByPlayer(player);
				if(!resultData.isSuccess()){
					sendChatMessage(player,RESPONSE_ERROR,resultData.getMessage());
					return;
				}
				Island2 island2 = resultData.getData();
				ResourceLocation dim = IslandUtils.getIslandDimensionLoca(island2.iid);
				ServerUtils.executeOnServerThread(()->{
					long gameTime = System.currentTimeMillis()-island2.ts.getTime();
					RuntimeWorldHandle worldHandle = Fantasy.get(RdiCoreServer.getServer()).getOrOpenPersistentWorld(dim, IslandUtils.getIslandWorldConfig(gameTime));
					ServerLevel world = worldHandle.asWorld();
					PlayerUtils.teleport(
							player,
							world,
							island2.loca.x,
							island2.loca.y+3,
							island2.loca.z,
							island2.loca.w,
							island2.loca.p);
					sendChatMessage(player,PlayerUtils.RESPONSE_SUCCESS,"已经回到了您的岛屿！");
				});
			});
            return 1;
        });
    }
}
