package calebzhou.rdi.core.server.mixin.gameplay;

import calebzhou.rdi.core.server.utils.PlayerUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

/**
 * Created by calebzhou on 2022-09-21,18:53.
 */
@Mixin(LivingEntity.class)
public abstract class mNoDroppingVoid {
	@Shadow
	public abstract boolean hurt(DamageSource source, float amount);

	@Overwrite
	public void outOfWorld() {
		if((LivingEntity)(Object)this instanceof Player player){
			PlayerUtils.INSTANCE.sendChatMessage(player, Component.literal("神秘的力量从虚空捞了你一把...").withStyle(ChatFormatting.GOLD).withStyle(ChatFormatting.ITALIC),true);
			player.teleportTo(player.getX(),320,player.getZ());
		}else{
			hurt(DamageSource.OUT_OF_WORLD, 114514.1919F);
		}
	}
}
