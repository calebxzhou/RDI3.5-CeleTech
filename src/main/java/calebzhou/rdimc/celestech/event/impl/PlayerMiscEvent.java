package calebzhou.rdimc.celestech.event.impl;

import calebzhou.rdimc.celestech.constant.ColorConstants;
import calebzhou.rdimc.celestech.event.PlayerChatCallback;
import calebzhou.rdimc.celestech.event.PlayerDeathCallback;
import calebzhou.rdimc.celestech.model.PlayerTemperature;
import calebzhou.rdimc.celestech.model.record.GenericRecord;
import calebzhou.rdimc.celestech.model.record.RecordType;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import calebzhou.rdimc.celestech.utils.ServerUtils;
import calebzhou.rdimc.celestech.utils.TextUtils;
import calebzhou.rdimc.celestech.utils.TimeUtils;
import net.minecraft.util.ActionResult;

//与服务器交互事件
public class PlayerMiscEvent {
    public PlayerMiscEvent() {
        //死亡事件
        PlayerDeathCallback.EVENT.register(((player, source) -> {
            HttpUtils.postObject(new GenericRecord(player.getUuidAsString(), RecordType.death, source.toString(), null,null));
            //还原体温
            PlayerTemperature.put(player.getEntityName(),PlayerTemperature.DEFAULT_TEMP);
            //随机掉落

            return ActionResult.PASS;
        }));

        PlayerChatCallback.EVENT.register(((player, message) -> {
            String msg = message.getRaw();
            HttpUtils.postObject(new GenericRecord(player.getUuidAsString(), RecordType.chat, player.getEntityName(), null,msg));
            ServerUtils.getAfkPlayerList().stream()
                    //玩家说的话里面有没有挂机人的名称
                    .filter(entry -> msg.contains(entry.getKey()))
                    .forEach(entry -> {
                        String name = entry.getKey();
                        int seconds = entry.getValue();
                        TextUtils.sendChatMessage(player, ColorConstants.GRAY+name+" 已经挂机 "+ TimeUtils.secondsToMinute(seconds,"分","秒")+" ,因此对方不一定能够及时回复您.");
                    });
            return ActionResult.PASS;
        }));
    }

}
