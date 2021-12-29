package calebzhou.rdimc.celestech.model.record;

import calebzhou.rdimc.celestech.utils.TimeUtils;

import java.io.Serializable;
import java.sql.Timestamp;

public class PlayerDeathRecord implements Serializable {
    String playerUuid;
    String damageSource;
    Timestamp deathTime;

    public PlayerDeathRecord(String playerUuid, String damageSource) {
        this.playerUuid = playerUuid;
        this.damageSource = damageSource;
        this.deathTime = TimeUtils.getNow();
    }
}
