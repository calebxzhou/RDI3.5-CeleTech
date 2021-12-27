package calebzhou.rdimc.celestech.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class ChatRecord implements Serializable {
    String playerUuid;
    String content;
    Timestamp oprTime;

    public ChatRecord(String playerUuid, String content, Timestamp oprTime) {
        this.playerUuid = playerUuid;
        this.content = content;
        this.oprTime = oprTime;
    }

    @Override
    public String toString() {
        return "ChatRecord{" +
                "playerUuid='" + playerUuid + '\'' +
                ", content='" + content + '\'' +
                ", oprTime=" + oprTime +
                '}';
    }
}
