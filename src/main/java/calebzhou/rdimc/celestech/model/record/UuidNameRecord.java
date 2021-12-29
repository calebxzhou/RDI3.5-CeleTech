package calebzhou.rdimc.celestech.model.record;

import java.io.Serializable;

public class UuidNameRecord implements Serializable {
    String playerUuid;
    String playerName;

    public UuidNameRecord(String playerUuid, String playerName) {
        this.playerUuid = playerUuid;
        this.playerName = playerName;
    }
}
