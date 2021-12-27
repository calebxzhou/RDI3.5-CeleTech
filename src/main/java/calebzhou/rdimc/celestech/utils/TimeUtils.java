package calebzhou.rdimc.celestech.utils;

import java.sql.Timestamp;

public class TimeUtils {
    public static Timestamp getNow(){
        return new Timestamp(System.currentTimeMillis());
    }
}
