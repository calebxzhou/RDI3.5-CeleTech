package calebzhou.rdimc.celestech.model;

import calebzhou.rdimc.celestech.constant.WorldConstants;
import com.google.gson.Gson;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

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
        CoordLocation location;
        try{
            location = new CoordLocation(split[0],Double.parseDouble(split[1]),Double.parseDouble(split[2]),Double.parseDouble(split[3]));
        }catch (ArrayIndexOutOfBoundsException e){
            return null;
        }
        return location;
    }
    //从玩家载入位置
    public static CoordLocation fromPlayer(ServerPlayerEntity player){
        return new CoordLocation(player.getWorld().getDimension().getEffects().toString(),
                (int)player.getX(), (int)player.getY(), (int)player.getZ());
    }
    public static CoordLocation fromBlockPos(String world, BlockPos bpos){
        return new CoordLocation(world,
                bpos.getX(), bpos.getY(),bpos.getZ());
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
    public int getPosiX() {
        return (int)posX;
    }

    public int getPosiY() {
        return (int)posY;
    }

    public int getPosiZ() {
        return (int)posZ;
    }
}
