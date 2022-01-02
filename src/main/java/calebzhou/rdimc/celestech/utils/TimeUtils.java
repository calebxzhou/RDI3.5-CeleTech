package calebzhou.rdimc.celestech.utils;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class TimeUtils {
    public static Timestamp getNow(){
        return new Timestamp(System.currentTimeMillis());
    }
    public static String getTimeChineseString(){
        LocalDateTime dateTime = LocalDateTime.now();
        int hour = dateTime.getHour();
        String charTime = "";
        if (hour >= 0 && hour <= 5)
            charTime = "凌晨";
        if (hour >= 6 && hour <= 8)
            charTime = "早上";
        if (hour >= 9 && hour <= 10)
            charTime = "上午";
        if (hour == 11 || hour == 12)
            charTime = "中午";
        if (hour >= 13 && hour <= 17)
            charTime = "下午";
        if (hour >= 18 && hour <= 23)
            charTime = "晚上";
        return charTime;
    }
    public static String secondsToMinute(int seconds,String split1,String split2){
        int min = seconds / 60;
        int sec = seconds - (min*60);
        return String.format("%s%s%s%s", min,split1, sec,split2);
    }
}
