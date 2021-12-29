package calebzhou.rdimc.celestech.model;

import calebzhou.rdimc.celestech.utils.TimeUtils;

import java.io.Serializable;
import java.sql.Timestamp;

public class Island implements Serializable {
    String islandId;
    String ownerUuid;
    CoordLocation location;
    Timestamp createTime;

    public Island(String islandId, String ownerUuid, CoordLocation location) {
        this.islandId = islandId;
        this.ownerUuid = ownerUuid;
        this.location = location;
        this.createTime = TimeUtils.getNow();
    }
}
