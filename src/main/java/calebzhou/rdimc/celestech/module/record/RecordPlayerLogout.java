package calebzhou.rdimc.celestech.module.record;

import calebzhou.rdimc.celestech.api.CallbackRegisterable;
import calebzhou.rdimc.celestech.event.PlayerDisconnectServerCallback;
import calebzhou.rdimc.celestech.model.record.GenericRecord;
import calebzhou.rdimc.celestech.model.record.RecordType;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import calebzhou.rdimc.celestech.utils.IdentifierUtils;
import calebzhou.rdimc.celestech.utils.ServerUtils;
import net.minecraft.util.ActionResult;

public class RecordPlayerLogout implements CallbackRegisterable {
    public RecordPlayerLogout() {

    }

    @Override
    public void registerCallbacks() {
        PlayerDisconnectServerCallback.EVENT.register(IdentifierUtils.byClass(this.getClass()),(player -> {
            HttpUtils.asyncSendObject(new GenericRecord(player.getUuidAsString(), RecordType.logout, null, null,null));
            ServerUtils.save();
            return ActionResult.PASS;
        }));
    }
}
