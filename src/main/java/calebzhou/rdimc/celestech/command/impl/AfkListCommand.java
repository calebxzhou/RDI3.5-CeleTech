package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.RdiMemoryStorage;
import calebzhou.rdimc.celestech.command.RdiCommand;
import calebzhou.rdimc.celestech.utils.TextUtils;
import calebzhou.rdimc.celestech.utils.ThreadPool;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;

public class AfkListCommand extends RdiCommand {
    public AfkListCommand() {
        super("afklist");
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getExecution() {
        return baseArgBuilder.executes(this::exec);
    }

    private int exec(CommandContext<CommandSourceStack> context) {
        ThreadPool.newThread(()->{
            CommandSourceStack source = context.getSource();

            TextUtils.sendChatMessage(source,"挂机列表:");
            RdiMemoryStorage.afkMap.forEach((pname, afkTicks)->{
                StringBuilder sb = new StringBuilder();
                float totalSecs = afkTicks / 20f;
                int hours = Math.round(totalSecs / 3600);
                int minutes = Math.round((totalSecs % 3600) / 60);
                int seconds = Math.round(totalSecs % 60);
                String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
                sb.append(pname).append(" ").append(timeString);
                TextUtils.sendChatMessage(source,sb.toString());
            });
            TextUtils.sendChatMessage(source,"--------");
        });


            return 1;
    }
}
