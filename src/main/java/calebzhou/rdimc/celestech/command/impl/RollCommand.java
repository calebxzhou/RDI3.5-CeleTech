package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.command.OneArgCommand;
import calebzhou.rdimc.celestech.constant.ColorConstants;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.model.ApiResponse;
import calebzhou.rdimc.celestech.model.RollPrize;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import calebzhou.rdimc.celestech.utils.PlayerUtils;
import calebzhou.rdimc.celestech.utils.TextUtils;
import net.minecraft.server.network.ServerPlayerEntity;
import org.apache.commons.lang3.RandomUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RollCommand extends OneArgCommand {
    public RollCommand(String name, int permissionLevel) {
        super(name, permissionLevel,true);
    }

    @Override
    protected void onExecute(ServerPlayerEntity player, String arg) {
        final int rollTimes = Integer.parseInt(arg);
        if(!PlayerUtils.checkExpLevel(player,rollTimes*3)){
            TextUtils.sendChatMessage(player,"您的经验不足，抽奖需要"+rollTimes*3+"级经验");
            return;
        }

        ApiResponse<RollPrize[]> response = HttpUtils.sendRequest("GET", "prize");
        RollPrize[] data = response.getData(RollPrize[].class);
        List<String> dataList = Arrays.stream(data).map(RollPrize::getDescr).collect(Collectors.toList());
        for(int i=0;i<rollTimes;++i){
            RollPrize prize = data[i];
            TextUtils.sendActionMessage(player,dataList.toString().substring(i));
            if(!prize.getPrizeSuccessful()){
                continue;
            }
            TextUtils.sendChatMessage(player,"恭喜您抽中了"+prize.getDescr(), MessageType.SUCCESS);
            if(prize.getProba()<0.1){
                TextUtils.sendGlobalChatMessage(player.getServer().getPlayerManager(), ColorConstants.GOLD+"恭喜"+player.getEntityName()+"抽中了"+ColorConstants.BRIGHT_GREEN+ColorConstants.BOLD+prize.getDescr());
            }

        }
    }
}
