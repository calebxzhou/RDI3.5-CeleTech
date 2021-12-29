package calebzhou.rdimc.celestech.model.thread;

import calebzhou.rdimc.celestech.RDICeleTech;
import calebzhou.rdimc.celestech.enums.ColorConst;
import calebzhou.rdimc.celestech.utils.*;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;

import java.io.Serializable;

public class PlayerMonitorThread extends Thread{
    private ServerPlayerEntity player;
    private String uuid;
    public PlayerMonitorThread(ServerPlayerEntity player){
        this.player=(player);
        this.uuid  = player.getUuidAsString();
    }
    @Override
    public void run() {
        //玩家下线则终止线程

        if(!ThreadPool.isPlayerThreadStarted(uuid)){
            this.interrupt();
            return;
        }
        if(player == null){
            this.interrupt();
            return;
        }
        long startTime = System.currentTimeMillis();
        PlayerMotionPath path1 = new PlayerMotionPath(player.getX(),player.getY(),player.getZ());
        try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
        player = RDICeleTech.getServer().getPlayerManager().getPlayer(player.getDisplayName().getString());
        if(player==null){
            System.out.println("玩家"+ player.getDisplayName().getString()+"下线！");
            this.interrupt();
            return;
        }
        long endTime = System.currentTimeMillis();
        PlayerMotionPath path2 = new PlayerMotionPath(player.getX(),player.getY(),player.getZ());
        double downSpeedLim = 2.0;// m/s
        double timeElapsedSec = (endTime-startTime) / 1000.0;
        //下落速度过快时
        if(Math.abs(path2.y-path1.y) / timeElapsedSec > downSpeedLim){
            TextUtils.sendActionMessage(player, ColorConst.GOLD+"感觉身体轻飘飘的...");
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING,40,1));
        }

        //重启线程
        try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
        new PlayerMonitorThread(player).run();
    }
}
class PlayerMotionPath implements Serializable{
    double x,y,z;

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
