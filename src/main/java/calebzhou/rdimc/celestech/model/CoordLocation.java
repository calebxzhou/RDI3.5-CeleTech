package calebzhou.rdimc.celestech.model;

import com.google.gson.Gson;
import net.minecraft.server.network.ServerPlayerEntity;

//坐标位置，XYZ
public class CoordLocation {
    double posX,posY,posZ;

    public CoordLocation(double posX, double posY, double posZ) {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s",posX,posY,posZ);
    }
    public static CoordLocation fromString(String string){
        String splitted[]=string.split(",");
        return new CoordLocation(Double.parseDouble(splitted[0]),Double.parseDouble(splitted[1]),Double.parseDouble(splitted[2]));
    }
    public static CoordLocation fromPlayer(ServerPlayerEntity player){
        return new CoordLocation(player.getX(), player.getY(), player.getZ());
    }
    public CoordLocation add(double x,double y,double z){
        return new CoordLocation(this.posX+x,this.posY+y,this.posZ+z);
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
