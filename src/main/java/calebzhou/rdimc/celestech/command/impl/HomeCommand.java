package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.RDICeleTech;
import calebzhou.rdimc.celestech.command.RdiCommand;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.model.PlayerLocation;
import calebzhou.rdimc.celestech.thread.RdiHttpPlayerRequest;
import calebzhou.rdimc.celestech.thread.RdiHttpRequest;
import calebzhou.rdimc.celestech.thread.RdiIslandRequestThread;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

import static calebzhou.rdimc.celestech.utils.PlayerUtils.teleport;
import static calebzhou.rdimc.celestech.utils.TextUtils.sendChatMessage;

public class HomeCommand extends RdiCommand {
    public HomeCommand() {
        super("home");
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getExecution() {
        return baseArgBuilder.executes(context -> {
            ServerPlayer player = context.getSource().getPlayer();
            RdiIslandRequestThread.addTask(new RdiHttpPlayerRequest(
                    RdiHttpRequest.Type.get,
                    player,
                    resp->{
                        if(resp.equals("fail")){
                            sendChatMessage(player, MessageType.ERROR,"您没加入任何一岛屿！");
                            return;
                        }
                        PlayerLocation loca = new PlayerLocation(resp);
                        loca.world= RDICeleTech.getServer().overworld();
                        player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING,20*30,1));
                        teleport(player, loca.add(0.5, 2, 0.5));
                        sendChatMessage(player,MessageType.SUCCESS,"1");
                    },
                    "island/" + player.getStringUUID()
            ));
            return 1;
        });
    }
}
