package calebzhou.rdimc.celestech.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class IslandS2CInfo implements Serializable {
    String ownerName;
    Timestamp createTime;
    String[] members;

    public IslandS2CInfo(String ownerName, Timestamp createTime, String[] members) {
        this.ownerName = ownerName;
        this.createTime = createTime;
        this.members = members;
    }
}
