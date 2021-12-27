package calebzhou.rdimc.celestech.model;

import com.google.gson.Gson;

import java.io.Serializable;
import java.sql.Timestamp;

public class BlockRecord implements Serializable {
    String playerUuid;
    String blockType;
    String blockAction;
    String dimension;
    Integer posX;
    Integer posY;
    Integer posZ;
    Timestamp oprTime;
    public String toString(){
        return new Gson().toJson(this);
    }

    public BlockRecord(String playerUuid, String blockType, String blockAction, String dimension, Integer posX, Integer posY, Integer posZ, Timestamp oprTime) {
        this.playerUuid = playerUuid;
        this.blockType = blockType;
        this.blockAction = blockAction;
        this.dimension = dimension;
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.oprTime = oprTime;
    }
}
