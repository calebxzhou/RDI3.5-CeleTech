package calebzhou.rdi.celestech.module.record;

import calebzhou.rdi.celestech.api.CallbackRegisterable;
import calebzhou.rdi.celestech.event.PlayerDeathCallback;
import calebzhou.rdi.celestech.model.record.GenericRecord;
import calebzhou.rdi.celestech.model.record.RecordType;
import calebzhou.rdi.celestech.utils.HttpUtils;
import calebzhou.rdi.celestech.utils.IdentifierUtils;
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
