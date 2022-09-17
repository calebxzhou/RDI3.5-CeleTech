package calebzhou.rdi.celestech.command.impl;

import calebzhou.rdi.celestech.RDICeleTech;
import calebzhou.rdi.celestech.command.ArgCommand;
import calebzhou.rdi.celestech.command.BaseCommand;
import calebzhou.rdi.celestech.constant.ColorConst;
import calebzhou.rdi.celestech.constant.MessageType;
import calebzhou.rdi.celestech.model.RollPrize;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.server.level.ServerPlayer;

public class RollCommand extends BaseCommand implements ArgCommand {
    public RollCommand(String name, int permissionLevel) {
        super(name, permissionLevel,true);
    }

    @Override
    public void onExecute(ServerPlayer player, String arg) {
        final int rollTimes = Integer.parseInt(arg);
        PlayerUtils.checkExpLevel(player,rollTimes);

        String resp = HttpUtils.sendRequestRaw("GET", "prize","count="+rollTimes);

        List<RollPrize> dataList = JsonUtils.stringToArray(resp,RollPrize[].class);
        List<String> itemStrList = dataList.stream().map(RollPrize::getDescr).collect(Collectors.toList());
        List<RollPrize> prizeSuccessList = new ArrayList<>();
        for(int i=0;i<rollTimes;++i){
            PlayerUtils.sendChatMessage(player,String.format("第%d次抽奖..",i+1),PlayerUtils.RESPONSE_INFO);
            final RollPrize prize = dataList.get(RDICeleTech.RANDOM.nextInt(0,dataList.size()-1));
            final List<String> itemStrListCopy = itemStrList;
            ThreadPool.newThread(()->{
                //随机打乱显示
                for(int j=0;j<10;++j){
                    List<String> itemStrListCopy2 = itemStrListCopy;
                    if(rollTimes%2==0)
                        itemStrListCopy2 = itemStrListCopy.stream().map(as-> ColorConst.GOLD + as).collect(Collectors.toList());
                    Collections.shuffle(itemStrListCopy2);
                    String itemStr = Arrays.toString(itemStrListCopy2.toArray());
                    TextUtils.sendActionMessage(player, itemStr.substring(itemStr.length()/2-10,itemStr.length()/2+10));
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            });

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            if(!prize.getPrizeSuccessful()){
                PlayerUtils.sendChatMessage(player, String.format("您什么都没有抽到。",i+1), PlayerUtils.RESPONSE_ERROR);
                continue;
            }
            PlayerUtils.sendChatMessage(player,"恭喜您抽中了"+prize.getDescr(), PlayerUtils.RESPONSE_SUCCESS);
            if(prize.getProba()<0.1){
                TextUtils.sendGlobalChatMessage(player.getServer().getPlayerList(), ColorConst.GOLD+"恭喜"+player.getScoreboardName()+"抽中了"+ ColorConst.BRIGHT_GREEN+ ColorConst.BOLD+prize.getDescr());
            }
            prizeSuccessList.add(prize);
        }
        if(prizeSuccessList.isEmpty()){
            PlayerUtils.sendChatMessage(player,"抽奖结束，您什么都没抽到。",PlayerUtils.RESPONSE_INFO);
            if(rollTimes>20){
                TextUtils.sendGlobalChatMessage(player.getServer().getPlayerList(), ColorConst.GOLD+player.getScoreboardName()+"抽了"+rollTimes+"次奖，什么都没抽到。");

            }
            return;
        }
        PlayerUtils.sendChatMessage(player,"正在向您发放奖品...",PlayerUtils.RESPONSE_INFO);
        prizeSuccessList.forEach(prize->{
            switch (prize.getType()){
                case item -> {
                    ServerUtils.executeCommandOnServer(String.format("give %s %s %d",player.getScoreboardName(),prize.getId(),prize.getCount()));
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
