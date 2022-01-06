package calebzhou.rdimc.celestech.model;

import calebzhou.rdimc.celestech.utils.TimeUtils;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

public class Island implements Serializable {
    String islandId;
    String ownerUuid;
    String location;

    Timestamp createTime;
    List<String> memIds;

    public List<String> getMemIds() {
        return memIds;
    }

    public Island(String islandId, String ownerUuid, CoordLocation location) {
        this.islandId = islandId;
        this.ownerUuid = ownerUuid;
        this.location = location.toString();
        this.createTime = TimeUtils.getNow();
    }

    public String getIslandId() {
        return islandId;
    }

    public String getOwnerUuid() {
        return ownerUuid;
    }

    public String getLocation() {
        return location;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }
}
