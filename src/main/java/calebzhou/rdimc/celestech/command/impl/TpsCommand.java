package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.RDICeleTech;
import calebzhou.rdimc.celestech.command.RdiCommand;
import calebzhou.rdimc.celestech.constant.ColorConst;
import calebzhou.rdimc.celestech.utils.MathUtils;
import calebzhou.rdimc.celestech.utils.ThreadPool;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;

import static calebzhou.rdimc.celestech.utils.TextUtils.sendChatMessage;

public class TpsCommand implements RdiCommand {
    @Override
    public String getName() {
        return "tps";
    }
    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getExecution() {
        return Commands.literal(getName()).executes(context -> exec(context.getSource()));
    }
   /* @Override
    protected void onExecute(ServerPlayer fromPlayer,String arg) {
        tps(fromPlayer);
        list(fromPlayer);
    }

    private void list(ServerPlayer player) {
        StringBuilder sb=new StringBuilder();
        ServerUtils.getAfkPlayerList().stream().forEach(e->{
            sb.append(e.getKey());
            sb.append("(挂机");
            sb.append(TimeUtils.secondsToMinute(e.getValue(),":",""));
            sb.append(")");

        });
        sendChatMessage(player,"挂机列表:"+(sb.length()==0?"无":sb.toString()));
    }*/
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
            SystemInfo systemInfo = new SystemInfo();
            HardwareAbstractionLayer hardware = systemInfo.getHardware();
            GlobalMemory memory = hardware.getMemory();
            long memoryTotal = memory.getTotal();
            long memoryAvailable = memory.getAvailable();
            long memoryUsed = memoryTotal-memoryAvailable;

            float memoryUsage =  (float)memoryUsed/(float)memoryTotal;
            sendChatMessage(player,String.format("Mem.%.1fMB/%dMB(%.1f%%)",displayMaxMemory*memoryUsage,displayMaxMemory,memoryUsage*100));
        });


        return 1;
    }
}