package calebzhou.rdimc.celestech.event.impl;

import calebzhou.rdimc.celestech.event.PlayerDeathCallback;
import calebzhou.rdimc.celestech.model.record.GenericRecord;
import calebzhou.rdimc.celestech.model.record.RecordType;
import calebzhou.rdimc.celestech.utils.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;

//与服务器交互事件
public class PlayerMiscEvent {

    public PlayerMiscEvent() {
        //死亡事件
        PlayerDeathCallback.EVENT.register(((player, source) -> {
            HttpUtils.asyncSendObject(new GenericRecord(player.getUuidAsString(), RecordType.death, source.toString(), null,null));
            //还原体温
            //PlayerTemperature.put(player.getEntityName(),PlayerTemperature.DEFAULT_TEMP);
            //随机掉落

            return ActionResult.PASS;
        }));


    }

    public void randomDropHandler(PlayerEntity player){

    }
}
