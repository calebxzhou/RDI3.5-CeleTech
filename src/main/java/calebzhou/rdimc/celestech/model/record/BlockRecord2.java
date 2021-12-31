package calebzhou.rdimc.celestech.model.record;

import calebzhou.rdimc.celestech.model.CoordLocation;
import calebzhou.rdimc.celestech.utils.TimeUtils;
import com.google.gson.Gson;

import java.io.Serializable;
import java.sql.Timestamp;

public class BlockRecord2 implements Serializable {
    String pid;
    String blockType;
    String blockAction;
    String location;
    Timestamp recTime;
    public String toString(){
        return new Gson().toJson(this);
    }

    public BlockRecord2(String pid, String blockType, Action blockAction, CoordLocation location) {
        this.pid = pid;
        this.blockType = blockType;
        this.blockAction = blockAction.toString();
        this.location = location.toString();
        this.recTime = TimeUtils.getNow();
    }
    public enum Action{
        PLACE,
        BREAK,
        PUT,
        TAKE
    }
}
