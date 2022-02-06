package calebzhou.rdimc.celestech.model;

import calebzhou.rdimc.celestech.utils.TimeUtils;
import com.google.gson.Gson;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

public class Island implements Serializable {
    String islandId;
    String ownerUuid;
    String location;
    Timestamp createTime;
    String[] members;

    public Island() {
    }

    public Island(String islandId, String ownerUuid, CoordLocation location) {
        this.islandId = islandId;
        this.ownerUuid = ownerUuid;
        this.location = location.toString();
        this.createTime = TimeUtils.getNow();
    }

    public Island(String islandId, String ownerUuid, String location, Timestamp time, String[] members) {
        this.islandId = islandId;
        this.ownerUuid = ownerUuid;
        this.location = location;
        this.createTime = time;
        this.members=members;
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

    public List<String> getMembers() {
        return Arrays.asList(members);
    }

    public void setMembers(String[] members) {
        this.members = members;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setIslandId(String islandId) {
        this.islandId = islandId;
    }

    public void setOwnerUuid(String ownerUuid) {
        this.ownerUuid = ownerUuid;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
