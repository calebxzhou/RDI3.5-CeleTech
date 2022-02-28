package calebzhou.rdimc.celestech.module.record;

import calebzhou.rdimc.celestech.api.CallbackRegisterable;
import calebzhou.rdimc.celestech.event.PlayerConnectServerCallback;
import calebzhou.rdimc.celestech.model.record.UuidNameRecord;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import net.minecraft.util.ActionResult;

public class RecordPlayerUuidName implements CallbackRegisterable {
    public RecordPlayerUuidName() {

    }

    @Override
    public void registerCallbacks() {
        PlayerConnectServerCallback.EVENT.register(((connection, player) -> {
            HttpUtils.asyncSendObject(new UuidNameRecord(player.getUuidAsString(), player.getEntityName()));
            return ActionResult.SUCCESS;
        }));
    }
}
