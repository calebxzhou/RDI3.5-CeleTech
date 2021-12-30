package calebzhou.rdimc.celestech.model.thread;

import calebzhou.rdimc.celestech.RDICeleTech;
import calebzhou.rdimc.celestech.constant.WorldConstants;
import calebzhou.rdimc.celestech.enums.ColorConst;
import calebzhou.rdimc.celestech.model.PlayerMotionPath;
import calebzhou.rdimc.celestech.utils.*;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;

import java.io.Serializable;

public class PlayerMonitorThread extends PlayerBaseThread{
    //限制下落速度2.0m/s
    private final double downSpeedLim = 2.0;// m/s

    public PlayerMonitorThread(ServerPlayerEntity player){
        super(player.getDisplayName().getString());
    }

    protected void execute() throws InterruptedException{
        handleDroppingVoid();
        handleDroppingFast();


        new PlayerMonitorThread(player).run();
    }

    private void handleDroppingVoid(){
        //掉入虚空
        if(player.getY()<-68){
            PlayerUtils.addSlowFallEffect(player);
            PlayerUtils.teleport(player, WorldConstants.SPAWN_LOCA);
        }
    }
    private void handleDroppingFast() throws InterruptedException {
        //下落速度过快时
        long startTime = System.currentTimeMillis();
        PlayerMotionPath path1 = new PlayerMotionPath(player);
        Thread.sleep(1000);
        player = PlayerUtils.getPlayerByName(player.getDisplayName().getString());
        long endTime = System.currentTimeMillis();
        PlayerMotionPath path2 = new PlayerMotionPath(player);
        double timeElapsedSec = (endTime-startTime) / 1000.0;
        //下落速度过快时
        if(Math.abs(path2.y-path1.y) / timeElapsedSec > downSpeedLim){
            TextUtils.sendActionMessage(player, ColorConst.GOLD+"感觉身体轻飘飘的...");
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING,40,1));
        }
    }
}
