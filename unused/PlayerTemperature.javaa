package calebzhou.rdimc.celestech.model;

import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;

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
    //玩家头顶是否露天
    protected static boolean isAffectedByDaylight(Player player) {
        if (player.level.isDay()) {
            float f = player.getBrightness();
            BlockPos blockPos = new BlockPos(player.getX(), player.getEyeY(), player.getZ());
            boolean bl = player.isInWaterRainOrBubble() || player.isInPowderSnow || player.wasInPowderSnow;
            if (f > 0.5F && player.getRandom().nextFloat() * 30.0F < (f - 0.4F) * 2.0F && !bl && player.level.canSeeSky(blockPos)) {
                return true;
            }
        }

        return false;
    }

}
