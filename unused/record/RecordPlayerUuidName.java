package calebzhou.rdi.celestech.module.record;

import calebzhou.rdi.celestech.api.CallbackRegisterable;
import calebzhou.rdi.celestech.event.PlayerConnectServerCallback;
import calebzhou.rdi.celestech.model.record.UuidNameRecord;
import calebzhou.rdi.celestech.utils.HttpUtils;
import calebzhou.rdi.celestech.utils.IdentifierUtils;
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
