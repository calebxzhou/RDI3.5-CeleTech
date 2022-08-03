package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.RDICeleTech;
import calebzhou.rdimc.celestech.command.RdiCommand;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.model.PlayerLocation;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import calebzhou.rdimc.celestech.utils.ThreadPool;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

import static calebzhou.rdimc.celestech.utils.PlayerUtils.teleport;
import static calebzhou.rdimc.celestech.utils.TextUtils.sendChatMessage;

public class HomeCommand implements RdiCommand {
    @Override
    public String getName() {
        return "home";
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getExecution() {
        return Commands.literal(getName()).executes(context -> exec(context.getSource().getPlayer()));
    }

    private int exec(ServerPlayer player) {
        ThreadPool.newThread(() -> {
            String resp = HttpUtils.sendRequest("get", "island/" + player.getStringUUID());
            if(resp.equals("fail")){
                sendChatMessage(player, MessageType.ERROR,"您没加入任何一岛屿！");
                return;
            }
            PlayerLocation loca = new PlayerLocation(resp);
            loca.world= RDICeleTech.getServer().overworld();
            player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING,20*30,1));
            teleport(player, loca.add(0.5, 2, 0.5));
            sendChatMessage(player,MessageType.SUCCESS,"1");
        });

        return 1;
    }


}
