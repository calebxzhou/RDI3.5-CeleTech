package calebzhou.rdi.core.server.command.impl;

import calebzhou.rdi.core.server.RdiCoreServer;
import calebzhou.rdi.core.server.RdiTickTaskManager;
import calebzhou.rdi.core.server.ServerLaggingStatus;
import calebzhou.rdi.core.server.command.RdiCommand;
import calebzhou.rdi.core.server.constant.ColorConst;
import calebzhou.rdi.core.server.utils.ThreadPool;
import calebzhou.rdi.core.server.utils.WorldUtils;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

import java.util.Arrays;
import static calebzhou.rdi.core.server.utils.PlayerUtils.sendMessageToCommandSource;

public class TpsCommand extends RdiCommand {
	public static final Object2ObjectOpenHashMap<String, Component> delayTickStatus = new Object2ObjectOpenHashMap<>();
	public TpsCommand() {
        super("tps","查询服务器的流畅程度");
    }
    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getExecution() {
        return baseArgBuilder.executes(context -> exec(context.getSource()));
    }

    final String squarePattern1 = ">";
    //100%负载tick时间
    final double stdTickTime = 70.0;
    final int displayMaxMemory=1023;
    private int exec(CommandSourceStack sourceStack) {
        ThreadPool.newThread(()->{
            //平均tick时间
            double meanTickTime = Arrays.stream(RdiCoreServer.getServer().tickTimes).average().getAsDouble() * 1.0E-6D;
            //平均tps
            double meanTPS = Math.min(1000.0 / meanTickTime, 20);
            //平均tick时间 比 100%负载tick时间
            double ratio = meanTickTime / stdTickTime;
            //字符数
            long squares = Math.round(25 * ratio);
            StringBuilder squaresToSend = new StringBuilder(ColorConst.BRIGHT_GREEN);
            for (int i = 0; i <= squares; ++i) {
                squaresToSend.append(squarePattern1);
                if (i == 15)
                    squaresToSend.append(ColorConst.GOLD);
                if (i == 22)
                    squaresToSend.append(ColorConst.RED);
            }
            sendMessageToCommandSource(sourceStack,"%d%%/%.2fTPS/%.2fms%s".formatted(Math.round(ratio * 100),meanTPS,meanTickTime, squaresToSend));
			String dimensionName = WorldUtils.getDimensionName(sourceStack.getLevel());
			int queueSize = RdiTickTaskManager.getQueueSize(dimensionName);
			sendMessageToCommandSource(sourceStack,
				"延迟?%s %sms 任务数%s "
					.formatted(
							ServerLaggingStatus.isServerLagging()?"是":"否",
							ServerLaggingStatus.getMsBehind(),
							queueSize
					)
			);
            long totalMemory = Runtime.getRuntime().totalMemory();
            long memoryUsed = totalMemory - Runtime.getRuntime().freeMemory();
            float memoryUsage =  (float)memoryUsed/(float)totalMemory;
			sendMessageToCommandSource(sourceStack,"ram=%.1fMB/%dMB(%.1f%%)".formatted(displayMaxMemory*memoryUsage,displayMaxMemory,memoryUsage*100));
			if(queueSize>0)
				sourceStack.sendSuccess(delayTickStatus.get(dimensionName),false);
        });


        return 1;
    }
}
