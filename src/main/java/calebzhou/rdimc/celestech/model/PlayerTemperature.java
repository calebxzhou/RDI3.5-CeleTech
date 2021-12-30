package calebzhou.rdimc.celestech.model;

import java.util.concurrent.ConcurrentHashMap;

public class PlayerTemperature {
    public static final float DEFAULT_TEMP = 35.5f;
    public static final float TEMP_DANGER = 37.3f;
    public static final float TEMP_DANGER2 = 38.0f;
    public static final float TEMP_DANGER3 = 38.9f;
    public static final float TEMP_DANGER4 = 39.6f;
    public static final float TEMP_WARN = 37.0f;
    public static final float TEMP_MAX = 42f;
    public static final float TEMP_MIN = 30f;
    private static final ConcurrentHashMap<String,Float> playerTempMap = new ConcurrentHashMap<>();
    public static void put(String playerName,float temp){
        playerTempMap.put(playerName,temp);
    }
    public static void add(String playerName,float tempToAdd){
        playerTempMap.put(playerName,playerTempMap.get(playerName) + tempToAdd);
    }
    public static void minus(String playerName,float tempToMinus){
        playerTempMap.put(playerName,playerTempMap.get(playerName) - tempToMinus);
    }
    public static void remove(String playerName){
        playerTempMap.remove(playerName);
    }
    public static boolean has(String playerName){
        return playerTempMap.containsKey(playerName);
    }
    public static float get(String playerName){
        return playerTempMap.get(playerName);
    }

}
