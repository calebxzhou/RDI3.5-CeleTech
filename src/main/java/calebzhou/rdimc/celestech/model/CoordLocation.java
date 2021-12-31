package calebzhou.rdimc.celestech.model;

import com.google.gson.Gson;
import net.minecraft.server.network.ServerPlayerEntity;

//坐标位置，XYZ
public class CoordLocation {
    public static final String OVERWORLD = "minecraft:overworld";
    String dimension;
    double posX,posY,posZ;

    public CoordLocation(double posX, double posY, double posZ) {
        this.dimension = OVERWORLD;
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
    }
    public CoordLocation(String dimension,double posX, double posY, double posZ) {
        this.dimension = dimension;
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
    }
    //从字符串载入位置
    public static CoordLocation fromString(String string){
        String[] split =string.split(",");
        return new CoordLocation(split[0],Double.parseDouble(split[1]),Double.parseDouble(split[2]),Double.parseDouble(split[3]));
    }
    //从玩家载入位置
    public static CoordLocation fromPlayer(ServerPlayerEntity player){
        return new CoordLocation(player.getWorld().getDimension().getEffects().toString(),
                (int)player.getX(), (int)player.getY(), (int)player.getZ());
    }
    public String toString() {
        return String.format("%s,%s,%s,%s",dimension,posX,posY,posZ);
    }

    public CoordLocation add(double x,double y,double z){
        return new CoordLocation(this.posX+x,this.posY+y,this.posZ+z);
    }
    public String getDimension(){
        return dimension;
    }
    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }

    public double getPosZ() {
        return posZ;
    }
    public double getPosiX() {
        return (int)posX;
    }

    public double getPosiY() {
        return (int)posY;
    }

    public double getPosiZ() {
        return (int)posZ;
    }
}
