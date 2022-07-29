package calebzhou.rdimc.celestech.module.record;

import calebzhou.rdimc.celestech.api.CallbackRegisterable;
import calebzhou.rdimc.celestech.event.PlayerChatCallback;
import calebzhou.rdimc.celestech.model.record.GenericRecord;
import calebzhou.rdimc.celestech.model.record.RecordType;
import calebzhou.rdimc.celestech.utils.EncodingUtils;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import calebzhou.rdimc.celestech.utils.IdentifierUtils;
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
