package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.RDICeleTech;
import calebzhou.rdimc.celestech.command.RdiCommand;
import calebzhou.rdimc.celestech.constant.ColorConst;
import calebzhou.rdimc.celestech.utils.MathUtils;
import calebzhou.rdimc.celestech.utils.ThreadPool;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;

import static calebzhou.rdimc.celestech.utils.TextUtils.sendChatMessage;

public class TpsCommand extends RdiCommand {
    public TpsCommand(   ) {
        super("tps");
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getExecution() {
        return baseArgBuilder.executes(context -> exec(context.getSource()));
    }

    final String squarePattern1 = ">";
    //100%负载tick时间
    final double stdTickTime = 70.0;
    final int displayMaxMemory=1023;
    private int exec(CommandSourceStack player) {
        ThreadPool.newThread(()->{
            //平均tick时间
            double meanTickTime = MathUtils.getAverageValue(RDICeleTech.getServer().tickTimes) * 1.0E-6D;
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
            sendChatMessage(player,String.format("%d%%/%.2fTPS/%.2fms%s",Math.round(ratio * 100),meanTPS,meanTickTime, squaresToSend));
           /* SystemInfo systemInfo = new SystemInfo();
            HardwareAbstractionLayer hardware = systemInfo.getHardware();
            GlobalMemory memory = hardware.getMemory();*/
            long totalMemory = Runtime.getRuntime().totalMemory();
            long memoryUsed = totalMemory - Runtime.getRuntime().freeMemory();
            float memoryUsage =  (float)memoryUsed/(float)totalMemory;
            sendChatMessage(player,String.format("m=%.1fMB/%dMB(%.1f%%) ",displayMaxMemory*memoryUsage,displayMaxMemory,memoryUsage*100));
        });


        return 1;
    }
}