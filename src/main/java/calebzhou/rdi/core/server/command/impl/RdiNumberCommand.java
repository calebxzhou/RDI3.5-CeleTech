package calebzhou.rdi.core.server.command.impl;

import calebzhou.rdi.core.server.command.RdiCommand;
import calebzhou.rdi.core.server.utils.PlayerUtils;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;

import static calebzhou.rdi.core.server.utils.PlayerUtils.*;


/**
 * Created by calebzhou on 2022-09-28,12:42.
 */
public class RdiNumberCommand extends RdiCommand {
	public RdiNumberCommand() {
		super("rdi-number","查询自己的RDI编号");
	}

	@Override
	public LiteralArgumentBuilder<CommandSourceStack> getExecution() {
		return baseArgBuilder.executes(this::exec);
	}

	private short exec(CommandContext<CommandSourceStack> context) {
		ServerPlayer player = context.getSource().getPlayer();
		String pid = player.getStringUUID();

		sendChatMessage(player, RESPONSE_INFO,"您的编号是：");
		sendChatMessage(player,Component.literal(pid).withStyle(style -> style.withColor(ChatFormatting.GREEN)
				.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, pid))
				.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("chat.copy.click")))
		));
		sendChatMessage(player,RESPONSE_INFO,"点击编号可以复制");
		return 1;
	}
}
