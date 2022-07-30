package calebzhou.rdimc.celestech.model;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;

public class PlayerLocation {
    public double x;
    public double y;
    public double z;
    public float w;
    public float p;
    public ServerLevel world;

    public PlayerLocation(double x, double y, double z, float w, float p, ServerLevel world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
        this.p = p;
        this.world = world;
    }

    public PlayerLocation(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public PlayerLocation(String x_comma_y_comma_z){
        String[] split = x_comma_y_comma_z.split(",");
        double x = Integer.parseInt(split[0]),y=Integer.parseInt(split[1]),z=Integer.parseInt(split[2]);
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public PlayerLocation(Player player){
        x=player.getX();
        y=player.getY();
        z=player.getZ();
        w=player.getYRot();
        p=player.getXRot();
        world= (ServerLevel) player.getLevel();
    }
    public PlayerLocation(BlockPos bpos,ServerLevel dim,float yaw,float pitch){
        x= bpos.getX();
        y= bpos.getY();
        z= bpos.getZ();
        w=yaw;
        p=pitch;
        world=dim;
    }

    public String getXyzComma(){
        return String.format("%s,%s,%s",x,y,z);
    }
    public PlayerLocation toInt(){
        return new PlayerLocation((int)x,(int)y,(int)z,(int)w,(int)p,world);
    }
    public PlayerLocation add(double x1,double y1,double z1){
        return new PlayerLocation(x+x1,y+y1,z+z1);
    }

}
