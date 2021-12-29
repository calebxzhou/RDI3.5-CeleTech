package calebzhou.rdimc.celestech.model.record;

import calebzhou.rdimc.celestech.utils.TimeUtils;

import java.io.Serializable;
import java.sql.Timestamp;

public class PlayerAttackRecord implements Serializable {
    String playerUuid;
    String damageSource;
    float hpDrain;
    Timestamp atkTime;

    public PlayerAttackRecord(String playerUuid, String damageSource,float hpDrain) {
        this.playerUuid = playerUuid;
        this.damageSource = damageSource;
        this.hpDrain = hpDrain;
        this.atkTime = TimeUtils.getNow();
    }
}
