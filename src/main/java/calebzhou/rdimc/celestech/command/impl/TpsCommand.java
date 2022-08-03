package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.RDICeleTech;
import calebzhou.rdimc.celestech.command.BaseCommand;
import calebzhou.rdimc.celestech.command.RdiCommand;
import calebzhou.rdimc.celestech.utils.MathUtils;
import calebzhou.rdimc.celestech.utils.ServerUtils;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

import static calebzhou.rdimc.celestech.utils.TextUtils.sendChatMessage;

public class TpsCommand implements RdiCommand {
    @Override
    public String getName() {
        return "tps";
    }
    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getExecution() {
        return Commands.literal(getName()).executes(context -> exec(context.getSource().getPlayer()));
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

    private int exec(ServerPlayer player) {
        double meanTickTime = MathUtils.getAverageValue(RDICeleTech.getServer().tickTimes) * 1.0E-6D;
        double stdTickTime = 120.0;
        double meanTPS = Math.min(1000.0 / meanTickTime, 20);
        double ratio = meanTickTime / stdTickTime;
        long squares = Math.round(25 * ratio);
        String squarePattern1 = ">";
        String squaresToSend = "§a";

        for (int i = 0; i <= squares; ++i) {
            squaresToSend += squarePattern1;
            if (i == 15)
                squaresToSend += "§e";
            if (i == 22)
                squaresToSend += "§c";
        }
        sendChatMessage(player,"[" + Math.round(ratio * 100) + "%/"+meanTPS+"tps]" + squaresToSend);
        sendChatMessage(player,Math.round(meanTickTime) + ".0ms");
        StringBuilder sb=new StringBuilder();
        ServerUtils.httpHistoryDelayList.stream().forEach((e)->{
            sb.append(String.format("%.2f",e/1000.0));
            sb.append("s, ");
        });
        double avg = ServerUtils.httpHistoryDelayList.stream()
                .mapToDouble(d -> d)
                .average()
                .orElse(0.0);
        sendChatMessage(player,"微服务延迟 "+sb.toString()+String.format("（平均 %.2f s）",avg/1000.0));
        return 1;
    }
}