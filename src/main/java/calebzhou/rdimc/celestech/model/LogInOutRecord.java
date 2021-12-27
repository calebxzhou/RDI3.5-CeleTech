package calebzhou.rdimc.celestech.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class LogInOutRecord implements Serializable {
    String playerUuid;
    String logAction;//IN OUT
    String ipAddr;
    Timestamp oprTime;

    public LogInOutRecord(String playerUuid, String inout, String ipAddr,Timestamp oprTime) {
        this.playerUuid = playerUuid;
        this.logAction = inout;
        this.ipAddr = ipAddr;
        this.oprTime = oprTime;
    }
}
