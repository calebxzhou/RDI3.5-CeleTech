package calebzhou.rdimc.celestech.model.thread;

import calebzhou.rdimc.celestech.constant.WorldConstants;
import calebzhou.rdimc.celestech.enums.ColorConst;
import calebzhou.rdimc.celestech.model.PlayerMotionPath;
import calebzhou.rdimc.celestech.utils.*;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.concurrent.ConcurrentHashMap;

public class PlayerMotionThread extends PlayerBaseThread{
    public static final ConcurrentHashMap<String,Integer> afkPlayersMap = new ConcurrentHashMap<>();
    //限制下落速度2.0m/s
    private final double downSpeedLim = 2.0;// m/s
    private int afkSeconds=0;
    private PlayerMotionPath path1,path2;
    public PlayerMotionThread(ServerPlayerEntity player){
        super(player.getDisplayName().getString());
    }

    protected void execute() throws InterruptedException{
        handleDroppingVoid();

        long startTime = System.currentTimeMillis();
        path1 = new PlayerMotionPath(player);
        Thread.sleep(1000);
        player = PlayerUtils.getPlayerByName(player.getDisplayName().getString());
        long endTime = System.currentTimeMillis();
        path2 = new PlayerMotionPath(player);


        handleDroppingFast((endTime-startTime) / 1000.0);
        handleAfk();

        new PlayerMotionThread(player).run();
    }

    private void handleDroppingVoid(){
        //掉入虚空
        if(player.getY()<-68){
            PlayerUtils.addSlowFallEffect(player);
            PlayerUtils.teleport(player, WorldConstants.SPAWN_LOCA);
        }
    }
    private void handleDroppingFast(double timeElapsedSec){
        //下落速度过快时
        if(Math.abs(path2.y-path1.y) / timeElapsedSec > downSpeedLim){
            TextUtils.sendActionMessage(player, ColorConst.GOLD+"感觉身体轻飘飘的...");
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING,40,1));
        }
    }
    private void handleAfk(){
        //这一秒没动,就增加挂机时间数
        if(path1.getVector().distanceTo(path2.getVector())<1){
            afkSeconds++;
        }
        if(afkSeconds > 60){
            afkPlayersMap.put(playerName,afkSeconds);
        }
    }
}
