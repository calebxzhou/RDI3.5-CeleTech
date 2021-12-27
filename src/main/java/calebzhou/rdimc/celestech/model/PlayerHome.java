package calebzhou.rdimc.celestech.model;

import com.google.gson.Gson;

import java.io.Serializable;

public class PlayerHome implements Serializable {
    String playerUuid;
    String homeName;
    String dimension;

    Double posX,posY,posZ;
    Float yaw,pitch;
    String comments;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
