package calebzhou.rdi.celestech.module.record;

import calebzhou.rdi.celestech.api.CallbackRegisterable;
import calebzhou.rdi.celestech.event.PlayerChatCallback;
import calebzhou.rdi.celestech.model.record.GenericRecord;
import calebzhou.rdi.celestech.model.record.RecordType;
import calebzhou.rdi.celestech.utils.EncodingUtils;
import calebzhou.rdi.celestech.utils.HttpUtils;
import calebzhou.rdi.celestech.utils.IdentifierUtils;
import net.minecraft.world.InteractionResult;

public class RecordPlayerChat implements CallbackRegisterable {
    public RecordPlayerChat() {

    }

    @Override
    public void registerCallbacks() {
        PlayerChatCallback.EVENT.register(IdentifierUtils.byClass(this.getClass()),(player, message) -> {
            String msg = message.getRaw();
            //上传消息
            GenericRecord cr = new GenericRecord(player.getStringUUID(), RecordType.chat, player.getScoreboardName(), null, EncodingUtils.getUTF8StringFromGBKString(msg));
            HttpUtils.asyncSendObject(cr);
            return InteractionResult.PASS;
        });
    }
}
