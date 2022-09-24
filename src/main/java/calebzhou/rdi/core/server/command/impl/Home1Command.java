package calebzhou.rdi.core.server.command.impl;

import calebzhou.rdi.core.server.command.RdiCommand;
import calebzhou.rdi.core.server.model.ResultData;
import calebzhou.rdi.core.server.utils.RdiHttpClient;
import calebzhou.rdi.core.server.utils.ServerUtils;
import calebzhou.rdi.core.server.utils.PlayerUtils;
import calebzhou.rdi.core.server.utils.ThreadPool;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

import static calebzhou.rdi.core.server.utils.PlayerUtils.sendChatMessage;

public class Home1Command extends RdiCommand {
	public Home1Command() {
        super("home1","回到一岛（仅限老玩家）");
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getExecution() {
        return baseArgBuilder.executes(context -> {
            ServerPlayer player = context.getSource().getPlayer();
			ThreadPool.newThread(()->{
				ResultData<String> resultData = RdiHttpClient.sendRequest(String.class,"get", "/v37/island/" + player.getStringUUID());
				if(resultData.getStatus()<0){
					PlayerUtils.sendServiceResultData(player,resultData);
					return;
				}
				String[] split = resultData.getData().split(",");
				int x= Integer.parseInt(split[0]);
				int y= Integer.parseInt(split[1]);
				int z= Integer.parseInt(split[2]);
				ServerUtils.executeOnServerThread(()->{
					player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING,20*30,1));
					PlayerUtils.teleport(player, player.getServer().overworld(),x+0.5,y+2,z+0.5,0,0);
				});
				sendChatMessage(player,PlayerUtils.RESPONSE_SUCCESS,"1");
			});

            return 1;
        });
    }
}
