package calebzhou.rdimc.celestech.module.record;

import calebzhou.rdimc.celestech.api.CallbackRegisterable;
import calebzhou.rdimc.celestech.event.PlayerConnectServerCallback;
import calebzhou.rdimc.celestech.model.record.GenericRecord;
import calebzhou.rdimc.celestech.model.record.RecordType;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import net.minecraft.util.ActionResult;

public class RecordPlayerLogin implements CallbackRegisterable {
    public RecordPlayerLogin() {

    }

    @Override
    public void registerCallbacks() {
        PlayerConnectServerCallback.EVENT.register((connection, player) -> {
            HttpUtils.asyncSendObject(new GenericRecord(player.getUuidAsString(), RecordType.login, player.getIp(), null,null));
            return ActionResult.SUCCESS;
        });
    }
}
