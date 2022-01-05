package calebzhou.rdimc.celestech.event.impl;

import calebzhou.rdimc.celestech.event.PlayerChatCallback;
import calebzhou.rdimc.celestech.model.cache.ChatRecordCache;
import calebzhou.rdimc.celestech.model.record.GenericRecord;
import calebzhou.rdimc.celestech.model.record.RecordType;
import calebzhou.rdimc.celestech.utils.EncodingUtils;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import calebzhou.rdimc.celestech.utils.ServerUtils;
import net.minecraft.util.ActionResult;

import java.util.HashSet;

public class PlayerChatEvent {
    public static final HashSet<String> censorship = new HashSet<>();
    //范围(时间) 玩家名 内容
    public static final String chatFormat = "%s|%s: %s";
    static{
        censorship.add("傻逼");
        censorship.add("操");
        censorship.add("nm");
        censorship.add("tm");
    }
    public PlayerChatEvent() {
        PlayerChatCallback.EVENT.register(((player, message) -> {
            String msg = message.getRaw();
            //上传消息
            GenericRecord cr=new GenericRecord(player.getUuidAsString(), RecordType.chat, player.getEntityName(), null, EncodingUtils.getUTF8StringFromGBKString(msg));
            //保存到缓存
            ChatRecordCache.instance.getRecordList().add(cr);
            HttpUtils.postObject(cr);
            //聊天 挂机玩家 提示
            if(msg.length()>=3) ServerUtils.getAfkPlayerListDo(entry -> entry.getKey().contains(msg),player);
            //censor
            if(censorship.stream().anyMatch(msg::contains)){

                return ActionResult.FAIL;
            }

            return ActionResult.PASS;
        }));
    }
}
