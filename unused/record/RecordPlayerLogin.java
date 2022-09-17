package calebzhou.rdi.celestech.module.record;

import calebzhou.rdi.celestech.api.CallbackRegisterable;
import calebzhou.rdi.celestech.event.PlayerConnectServerCallback;
import calebzhou.rdi.celestech.model.record.GenericRecord;
import calebzhou.rdi.celestech.model.record.RecordType;
import calebzhou.rdi.celestech.utils.HttpUtils;
import calebzhou.rdi.celestech.utils.IdentifierUtils;
import net.minecraft.world.InteractionResult;

public class RecordPlayerLogin implements CallbackRegisterable {
    public RecordPlayerLogin() {

    }

    @Override
    public void registerCallbacks() {
        PlayerConnectServerCallback.EVENT.register(IdentifierUtils.byClass(this.getClass()),(connection, player) -> {
            HttpUtils.asyncSendObject(new GenericRecord(player.getStringUUID(), RecordType.login, player.getIpAddress(), null,null));
            return InteractionResult.PASS;
        });
    }
}
