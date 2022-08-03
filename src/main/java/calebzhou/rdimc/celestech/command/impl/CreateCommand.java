package calebzhou.rdimc.celestech.command.impl;

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
import net.minecraft.world.level.block.Blocks;

import static calebzhou.rdimc.celestech.utils.PlayerUtils.*;
import static calebzhou.rdimc.celestech.utils.TextUtils.sendChatMessage;

public class CreateCommand implements RdiCommand {

    @Override
    public String getName() {
        return "create";
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getExecution() {
        return Commands.literal(getName()).executes(context -> exec(context.getSource().getPlayer()));
    }

    private int exec(ServerPlayer player) {
        ThreadPool.newThread(()->{
            String resp = HttpUtils.sendRequest("post", "island/" + player.getStringUUID());
            if(resp.equals("fail")){
                sendChatMessage(player,MessageType.ERROR,"您已经加入了一个岛屿！");
                return;
            }
            PlayerLocation loca = new PlayerLocation(resp);
            loca.world= player.getLevel();
            player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING,20*30,1));
            teleport(player, loca.add(0.5, 12, 0.5));
            placeBlock(player.getLevel(), loca, Blocks.OBSIDIAN.defaultBlockState());
            placeBlock(player.getLevel(), loca.add(-1,0,0), Blocks.GRASS_BLOCK.defaultBlockState());
            givePlayerInitialKit(player);
            sendChatMessage(player,MessageType.SUCCESS,"1");
        });
        return 1;
    }

}
