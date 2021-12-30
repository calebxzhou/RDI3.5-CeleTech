package calebzhou.rdimc.celestech.model;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;

import java.io.Serializable;

public class PlayerMotionPath implements Serializable {
    public double x,y,z;

    public PlayerMotionPath(PlayerEntity player){
        this.x= player.getX();
        this.y= player.getY();
        this.z= player.getZ();
    }
    public PlayerMotionPath(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    Vec3d getVector(){
        return new Vec3d(x,y,z);
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
