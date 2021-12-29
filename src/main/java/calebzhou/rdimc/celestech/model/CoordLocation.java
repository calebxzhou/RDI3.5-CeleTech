package calebzhou.rdimc.celestech.model;

import com.google.gson.Gson;

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
        return new Gson().toJson(this);
    }
    public static CoordLocation fromString(String json){
        return new Gson().fromJson(json,CoordLocation.class);
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
