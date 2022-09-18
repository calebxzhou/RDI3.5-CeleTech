package calebzhou.rdi.core.server.command.impl;

import calebzhou.rdi.core.server.command.RdiCommand;
import calebzhou.rdi.core.server.utils.PlayerUtils;
import calebzhou.rdi.core.server.utils.ServerUtils;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;

public class DragonCommand extends RdiCommand {
	static{
		RdiCommand.register(new DragonCommand());
	}
    private DragonCommand() {
        super("dragon","召唤一只末影龙");
    }
	private final int expLvlNeed = 100;
    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getExecution() {
        return baseArgBuilder.executes(context -> {
			ServerPlayer player = context.getSource().getPlayer();
			if(!PlayerUtils.isInTheEnd(player)){
				PlayerUtils.sendChatMessage(player, PlayerUtils.RESPONSE_ERROR,"必须在末地执行本指令");
				return 1;
			}
			if(player.experienceLevel<expLvlNeed){
				PlayerUtils.sendChatMessage(player,PlayerUtils.RESPONSE_ERROR,"您需要有"+expLvlNeed+"级经验才能召唤神龙！");
				return 1;
			}
			PlayerUtils.sendChatMessage(player,PlayerUtils.RESPONSE_SUCCESS,"成功召唤神龙！");
			ServerUtils.spawnEntity(EntityType.ENDER_DRAGON,player.getLevel(),player.getOnPos().above(50));
			player.experienceLevel-=expLvlNeed;
			return 1;
		});
    }
}
