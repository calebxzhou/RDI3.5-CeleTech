package calebzhou.rdimc.celestech.utils;

public class MathUtils {
    public static boolean between(double x,double a,double y){
        return x<a && y>a && x<y;
    }
    public static long getAverageValue(long[] values){
        long sum = 0L;
        for (long v : values)
            sum += v;
        return sum / values.length;
    }
}
