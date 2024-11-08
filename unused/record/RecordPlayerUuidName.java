package calebzhou.rdimc.celestech.module.record;

import calebzhou.rdimc.celestech.api.CallbackRegisterable;
import calebzhou.rdimc.celestech.event.PlayerConnectServerCallback;
import calebzhou.rdimc.celestech.model.record.UuidNameRecord;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import calebzhou.rdimc.celestech.utils.IdentifierUtils;
import net.minecraft.world.InteractionResult;

public class RecordPlayerUuidName implements CallbackRegisterable {
    public RecordPlayerUuidName() {

    }

    @Override
    public void registerCallbacks() {
        PlayerConnectServerCallback.EVENT.register(IdentifierUtils.byClass(this.getClass()),((connection, player) -> {
            HttpUtils.asyncSendObject(new UuidNameRecord(player.getStringUUID(), player.getScoreboardName()));
            return InteractionResult.PASS;
        }));
    }
}
