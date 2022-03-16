package calebzhou.rdimc.celestech.module.record;

import calebzhou.rdimc.celestech.api.CallbackRegisterable;
import calebzhou.rdimc.celestech.event.PlayerDeathCallback;
import calebzhou.rdimc.celestech.model.record.GenericRecord;
import calebzhou.rdimc.celestech.model.record.RecordType;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import calebzhou.rdimc.celestech.utils.IdentifierUtils;
import net.minecraft.world.InteractionResult;

public class RecordPlayerDeath implements CallbackRegisterable {
    public RecordPlayerDeath() {

    }

    @Override
    public void registerCallbacks() {
        PlayerDeathCallback.EVENT.register(IdentifierUtils.byClass(this.getClass()),((player, source) -> {
            HttpUtils.asyncSendObject(new GenericRecord(player.getStringUUID(), RecordType.death, source.toString(), null,null));
            return InteractionResult.PASS;
        }));
    }
}
