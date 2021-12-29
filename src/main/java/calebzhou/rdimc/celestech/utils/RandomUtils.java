package calebzhou.rdimc.celestech.utils;

import calebzhou.rdimc.celestech.model.CoordLocation;

import java.util.Random;

public class RandomUtils {
    public static int generateRandomInt(int min, int max) {
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }
    public static boolean randomPercentage(double perc){
        int ranMax=10000;
        int ranPerc=(int)(ranMax*perc);
        int ran=RandomUtils.generateRandomInt(0,ranMax);
        if(ran<ranPerc)
            return true;
        else
            return false;
    }
    public static String getRandomId(int digits){
        StringBuilder sb= new StringBuilder();
        for(int i=0;i<digits;i++){
            sb.append(generateRandomInt(0,9));
        }
        return sb.toString();
    }
    public static CoordLocation getRandomCoordinate(){
        int x=RandomUtils.generateRandomInt(-9999,9999);
        int z=RandomUtils.generateRandomInt(-9999,9999);
        return new CoordLocation(x,220,z);
    }
}
