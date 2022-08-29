package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.RDICeleTech;
import calebzhou.rdimc.celestech.command.RdiCommand;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.model.PlayerLocation;
import calebzhou.rdimc.celestech.thread.RdiHttpPlayerRequest;
import calebzhou.rdimc.celestech.thread.RdiHttpRequest;
import calebzhou.rdimc.celestech.thread.RdiRequestThread;
import calebzhou.rdimc.celestech.utils.ServerUtils;
import calebzhou.rdimc.celestech.utils.TextUtils;
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
            TextUtils.sendChatMessage(player,MessageType.INFO,"建议使用/is2 create & /home2指令，将岛屿迁移至独立的存档（“二岛”），以支持未来推出的刷怪控制、防爆、耐火、岛屿积分计算、权限控制等高级特性。");
            RdiRequestThread.addTask(new RdiHttpPlayerRequest(
                    RdiHttpRequest.Type.get,
                    player,
                    resp->{
                        if(resp.equals("fail")){
                            sendChatMessage(player, MessageType.ERROR,"您没加入任何一岛屿！");
                            return;
                        }
                        sendChatMessage(player,MessageType.SUCCESS,resp);
                        String[] split = resp.split(",");
                        int x= Integer.parseInt(split[0]);
                        int y= Integer.parseInt(split[1]);
                        int z= Integer.parseInt(split[2]);
                        ServerUtils.executeOnServerThread(()->{
                            player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING,20*30,1));
                            teleport(player, player.getServer().overworld(),x+0.5,y+2,z+0.5,0,0);
                        });
                        sendChatMessage(player,MessageType.SUCCESS,"1");
                    },
                    "island/" + player.getStringUUID()
            ));
            return 1;
        });
    }
}
