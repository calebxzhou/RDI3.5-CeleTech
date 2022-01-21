package calebzhou.rdimc.celestech.event.impl;

import calebzhou.rdimc.celestech.RDICeleTech;
import calebzhou.rdimc.celestech.event.PlayerChatCallback;
import calebzhou.rdimc.celestech.model.record.GenericRecord;
import calebzhou.rdimc.celestech.model.record.RecordType;
import calebzhou.rdimc.celestech.utils.EncodingUtils;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import calebzhou.rdimc.celestech.utils.ServerUtils;
import calebzhou.rdimc.celestech.utils.TextUtils;
import net.minecraft.util.ActionResult;

import java.util.HashSet;

public class PlayerChatEvent {

    public static final HashSet<String> censorship = new HashSet<>();
    //范围(时间) 玩家名 内容
    public static final String chatFormat = "%s %s: %s";

    static {
        censorship.add("傻");
        censorship.add("逼");
        censorship.add("操");
        censorship.add("nm");
        censorship.add("tm");
    }


    public PlayerChatEvent() {
        PlayerChatCallback.EVENT.register(((player, message) -> {
            String msg = message.getRaw();
            //上传消息
            GenericRecord cr = new GenericRecord(player.getUuidAsString(), RecordType.chat, player.getEntityName(), null, EncodingUtils.getUTF8StringFromGBKString(msg));
            HttpUtils.asyncSendObject(cr);
            //聊天 挂机玩家 提示
            if (msg.length() >= 3) ServerUtils.getAfkPlayerListDo(entry -> entry.getKey().contains(msg), player);
            //censor
            if (censorship.stream().anyMatch(msg::contains)) {
                return ActionResult.FAIL;
            }
            RDICeleTech.getServer().getPlayerManager().getPlayerList().forEach(p -> {
                TextUtils.sendChatMessage(p, String.format(chatFormat,
                        "全> ",//status.getRange().getDesp(),
                        player.getEntityName(),
                        msg));
            });
            //broadcast(player,msg);
            return ActionResult.PASS;
        }));
    }

/*    private void broadcast(ServerPlayerEntity player, String msg) {
        String senderName = player.getEntityName();
        HashMap<String, ChatStatus> statusMap = ChatRangeCache.instance.getMap();
        List<ServerPlayerEntity> receivePlayerList = new ArrayList<>();
        *//*
        玩家发言状态: 收-全/岛/无, 发-全/岛/无
        收-全 发-全 √   收-岛 发-全 ×   收-无 发-无 √
        收-全 发-岛 √   收-岛 发-岛 √
        收-全 发-无 ×   收-岛 发-无 ×
        *//*
        ChatStatus status = statusMap.get(senderName);
        //静音
        if (status.getRange() == ChatRange.mute) {
            TextUtils.sendChatMessage(player, "您目前处于静音模式, 因此您无法发送消息.", MessageType.ERROR);
            return;
        }
        //全部
        if (status.getRange() == ChatRange.all) {
            receivePlayerList = RDICeleTech.getServer().getPlayerManager().getPlayerList();
        } else if (status.getRange() == ChatRange.team) {//岛内
            Island island = IslandCache.instance.getIslandMap().get(player.getUuidAsString());
            //成员
           *//* receivePlayerList = island.getMemIds()
                    .stream().map(id -> PlayerUtils.getPlayerByUuid(id)).collect(Collectors.toList());*//*
            //岛主
            receivePlayerList.add(PlayerUtils.getPlayerByUuid(island.getOwnerUuid()));
        }
        receivePlayerList.forEach(p -> {
            TextUtils.sendChatMessage(p, String.format(chatFormat,
                    status.getRange().getDesp(),
                    p.getEntityName(),
                    msg));
        });
    }*/

}
