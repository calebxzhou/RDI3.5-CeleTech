package calebzhou.rdimc.celestech.module.record;

import calebzhou.rdimc.celestech.api.CallbackRegisterable;
import calebzhou.rdimc.celestech.event.PlayerDeathCallback;
import calebzhou.rdimc.celestech.model.record.GenericRecord;
import calebzhou.rdimc.celestech.model.record.RecordType;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import net.minecraft.util.ActionResult;

public class RecordPlayerDeath implements CallbackRegisterable {
    public RecordPlayerDeath() {

    }

    @Override
    public void registerCallbacks() {
        PlayerDeathCallback.EVENT.register(((player, source) -> {
            HttpUtils.asyncSendObject(new GenericRecord(player.getUuidAsString(), RecordType.death, source.toString(), null,null));
            return ActionResult.PASS;
        }));
    }
}
