package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.RDICeleTech;
import calebzhou.rdimc.celestech.command.NoArgCommand;
import calebzhou.rdimc.celestech.utils.MathUtils;
import calebzhou.rdimc.celestech.utils.ServerUtils;
import calebzhou.rdimc.celestech.utils.TimeUtils;
import net.minecraft.server.network.ServerPlayerEntity;

import static calebzhou.rdimc.celestech.utils.TextUtils.sendChatMessage;

public class TpsCommand extends NoArgCommand {
    public TpsCommand(String name, int permissionLevel) {
        super(name, permissionLevel);
    }

    @Override
    protected void onExecute(ServerPlayerEntity fromPlayer) {
        tps(fromPlayer);
        list(fromPlayer);
    }

    private void list(ServerPlayerEntity player) {
        StringBuilder sb=new StringBuilder();
        ServerUtils.getAfkPlayerList().stream().forEach(e->{
            sb.append(e.getKey());
            sb.append("(挂机");
            sb.append(TimeUtils.secondsToMinute(e.getValue(),":",""));
            sb.append(")");

        });
        sendChatMessage(player,"挂机列表:"+(sb.length()==0?"无":sb.toString()));
    }

    private void tps(ServerPlayerEntity player) {
        double meanTickTime = MathUtils.getAverageValue(RDICeleTech.getServer().lastTickLengths) * 1.0E-6D;
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
    }
}