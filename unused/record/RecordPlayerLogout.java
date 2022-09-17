package calebzhou.rdi.celestech.module.record;

import calebzhou.rdi.celestech.api.CallbackRegisterable;
import calebzhou.rdi.celestech.event.PlayerDisconnectServerCallback;
import calebzhou.rdi.celestech.model.record.GenericRecord;
import calebzhou.rdi.celestech.model.record.RecordType;
import calebzhou.rdi.celestech.utils.HttpUtils;
import calebzhou.rdi.celestech.utils.IdentifierUtils;
import calebzhou.rdi.celestech.utils.ServerUtils;
import net.minecraft.world.InteractionResult;

public class RecordPlayerLogout implements CallbackRegisterable {
    public RecordPlayerLogout() {

    }

    @Override
    public void registerCallbacks() {
        PlayerDisconnectServerCallback.EVENT.register(IdentifierUtils.byClass(this.getClass()),(player -> {
            HttpUtils.asyncSendObject(new GenericRecord(player.getStringUUID(), RecordType.logout, null, null,null));
            ServerUtils.save();
            return InteractionResult.PASS;
        }));
    }
}
