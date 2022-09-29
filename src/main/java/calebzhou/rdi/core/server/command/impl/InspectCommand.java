package calebzhou.rdi.core.server.command.impl;

import calebzhou.rdi.core.server.command.RdiCommand;
import calebzhou.rdi.core.server.utils.RdiHttpClient;
import calebzhou.rdi.core.server.utils.ThreadPool;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

/**
 * Created by calebzhou on 2022-09-29,19:59.
 */
public class InspectCommand extends RdiCommand {
	public InspectCommand() {
		super("inspect");
	}

	@Override
	public LiteralArgumentBuilder<CommandSourceStack> getExecution() {
		return baseArgBuilder.executes(context -> {
			ServerPlayer player = context.getSource().getPlayer();
			/*ThreadPool.newThread(()->{
				RdiHttpClient.
			});
*/

			return 1;
		});
	}
}
