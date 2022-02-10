package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.RDICeleTech;
import calebzhou.rdimc.celestech.command.OneArgCommand;
import calebzhou.rdimc.celestech.constant.ColorConstants;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.model.ApiResponse;
import calebzhou.rdimc.celestech.model.RollPrize;
import calebzhou.rdimc.celestech.utils.*;
import lombok.SneakyThrows;
import net.minecraft.server.network.ServerPlayerEntity;
import org.apache.commons.lang3.RandomUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RollCommand extends OneArgCommand {
    public RollCommand(String name, int permissionLevel) {
        super(name, permissionLevel,true);
    }

    @Override
    protected void onExecute(ServerPlayerEntity player, String arg) {
        final int rollTimes = Integer.parseInt(arg);
        if(!PlayerUtils.checkExpLevel(player,rollTimes)){
            TextUtils.sendChatMessage(player,"您的经验不足，抽奖需要"+rollTimes+"级经验");
            return;
        }

        String resp = HttpUtils.sendRequestRaw("GET", "prize","count="+rollTimes);

        List<RollPrize> dataList = JsonUtils.stringToArray(resp,RollPrize[].class);
        List<String> itemStrList = dataList.stream().map(RollPrize::getDescr).collect(Collectors.toList());
        List<RollPrize> prizeSuccessList = new ArrayList<>();
        for(int i=0;i<rollTimes;++i){
            TextUtils.sendChatMessage(player,String.format("第%d次抽奖..",i+1),MessageType.INFO);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            RollPrize prize = dataList.get(RDICeleTech.RANDOM.nextInt(0,dataList.size()-1));
            List<String> itemStrListCopy = itemStrList;
            //随机打乱显示
            if(rollTimes%2==0)
                itemStrListCopy = itemStrListCopy.stream().map(as-> ColorConstants.GOLD + as).collect(Collectors.toList());
            Collections.shuffle(itemStrListCopy);
            String itemStr = Arrays.toString(itemStrListCopy.toArray());


            TextUtils.sendActionMessage(player, itemStr.substring(itemStr.length()/2-10,itemStr.length()/2+10));
            if(!prize.getPrizeSuccessful()){
                TextUtils.sendChatMessage(player, String.format("您什么都没有抽到。",i+1), MessageType.ERROR);
                continue;
            }
            TextUtils.sendChatMessage(player,"恭喜您抽中了"+prize.getDescr(), MessageType.SUCCESS);
            if(prize.getProba()<0.1){
                TextUtils.sendGlobalChatMessage(player.getServer().getPlayerManager(), ColorConstants.GOLD+"恭喜"+player.getEntityName()+"抽中了"+ColorConstants.BRIGHT_GREEN+ColorConstants.BOLD+prize.getDescr());
            }
            prizeSuccessList.add(prize);
        }
        if(prizeSuccessList.isEmpty()){
            TextUtils.sendChatMessage(player,"抽奖结束，您什么都没抽到。",MessageType.INFO);
            if(rollTimes>20){
                TextUtils.sendGlobalChatMessage(player.getServer().getPlayerManager(), ColorConstants.GOLD+player.getEntityName()+"抽了"+rollTimes+"次奖，什么都没抽到。");

            }
            return;
        }
        TextUtils.sendChatMessage(player,"正在向您发放奖品...",MessageType.INFO);
        prizeSuccessList.forEach(prize->{
            switch (prize.getType()){
                case item -> {
                    ServerUtils.executeCommandOnServer(String.format("give %s %s %d",player.getEntityName(),prize.getId(),prize.getCount()));
                }
                case exp -> {
                    player.experienceLevel+=prize.getCount();
                }
                case creature -> {
                    ServerUtils.executeCommandOnServer(String.format("summon %s %f %f %f", prize.getId(),player.getX(),player.getY(),player.getZ()));
                }
            }
        });

    }
}
