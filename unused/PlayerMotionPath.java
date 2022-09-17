package calebzhou.rdi.celestech.model;

import java.io.Serializable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class PlayerMotionPath implements Serializable {
    public double x,y,z;

    public PlayerMotionPath(Player player){
        this.x= player.getX();
        this.y= player.getY();
        this.z= player.getZ();
    }
    public PlayerMotionPath(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public Vec3 getVector(){
        return new Vec3(x,y,z);
    }

    @Override
    public String toString() {
        return "PlayerMotionPath{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
