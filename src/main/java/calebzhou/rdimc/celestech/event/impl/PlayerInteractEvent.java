package calebzhou.rdimc.celestech.event.impl;

import calebzhou.rdimc.celestech.event.PlayerDeathCallback;
import calebzhou.rdimc.celestech.model.PlayerTemperature;
import calebzhou.rdimc.celestech.model.record.GenericRecord;
import calebzhou.rdimc.celestech.model.record.RecordType;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import net.minecraft.util.ActionResult;

public class PlayerInteractEvent {
    public PlayerInteractEvent() {
        //死亡事件
        PlayerDeathCallback.EVENT.register(((player, source) -> {
            HttpUtils.postObject(new GenericRecord(player.getUuidAsString(), RecordType.death, source.toString(), null,null));
            //还原体温
            PlayerTemperature.put(player.getEntityName(),PlayerTemperature.DEFAULT_TEMP);
            return ActionResult.PASS;
        }));
    }
}
