package calebzhou.rdimc.celestech.model;

import calebzhou.rdimc.celestech.utils.TimeUtils;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.List;

public class Island implements Serializable {
    String islandId;
    String ownerUuid;
    String location;


    Timestamp createTime;

    @Override
    public String toString() {
        return new Gson().toJson(this);
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

    public static Island fromString(String data){
        Type typeToken = new TypeToken<ApiResponse<Island>>() { }.getType();
        ApiResponse<Island> targetObject = new Gson().fromJson(data, typeToken);
        return targetObject.getData(Island.class);
    }
}
