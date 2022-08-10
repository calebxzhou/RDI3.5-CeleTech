package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.command.RdiCommand;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.utils.TextUtils;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;

public class DragonCommand implements RdiCommand {
    @Override
    public String getName() {
        return "dragon";
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getExecution() {
        return Commands.literal(getName())
                .executes(context -> exec(context.getSource().getPlayer()));
    }
    private final int expLvlNeed = 100;
    private int exec(ServerPlayer player) {
        if(!player.getLevel().dimension().location().toString().equals("minecraft:the_end")){
            TextUtils.sendChatMessage(player, MessageType.ERROR,"必须在末地执行本指令");
            return 1;
        }
        if(player.experienceLevel<expLvlNeed){
            TextUtils.sendChatMessage(player,MessageType.ERROR,"您需要有"+expLvlNeed+"级经验才能召唤神龙！");
            return 1;
        }
        TextUtils.sendChatMessage(player,MessageType.SUCCESS,"成功召唤神龙！");
        EntityType.ENDER_DRAGON.spawn(player.getLevel(), new CompoundTag(), Component.literal(player.getScoreboardName()+"召唤的神龙"),player,player.blockPosition().above(30), MobSpawnType.SPAWN_EGG,false,false);
        player.experienceLevel-=expLvlNeed;
        return 1;
    }
}
