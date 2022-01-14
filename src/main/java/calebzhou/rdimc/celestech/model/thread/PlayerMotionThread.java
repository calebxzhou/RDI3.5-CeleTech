package calebzhou.rdimc.celestech.model.thread;

import calebzhou.rdimc.celestech.RDICeleTech;
import calebzhou.rdimc.celestech.constant.WorldConstants;
import calebzhou.rdimc.celestech.constant.ColorConstants;
import calebzhou.rdimc.celestech.model.PlayerMotionPath;
import calebzhou.rdimc.celestech.utils.*;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.concurrent.ConcurrentHashMap;

public class PlayerMotionThread extends PlayerBaseThread{
    //玩家名\挂机秒数
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


        if(player.getHealth()>20) player.setHealth(20.0f);

        if(PlayerUtils.getDimensionName(player).equals(WorldConstants.OVERWORLD)){
            long startTime = System.currentTimeMillis();
            path1 = new PlayerMotionPath(player);
            Thread.sleep(500);
            player = PlayerUtils.getPlayerByName(player.getDisplayName().getString());
            long endTime = System.currentTimeMillis();
            path2 = new PlayerMotionPath(player);
            handleDroppingFast((endTime-startTime) / 1000.0);
        }

        handleAfk();

        run();
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
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING,20,0));
        }
    }
    private void handleAfk(){
        if(path1.getVector().distanceTo(path2.getVector())>1){
            afkSeconds = 0;
            afkPlayersMap.remove(playerName);
        }
        //这一秒没动,就增加挂机时间数
        else if(path1.getVector().distanceTo(path2.getVector())<=1
                || player.isPushedByFluids()){
            ++afkSeconds;
        }
        if(afkSeconds > 60){
            afkPlayersMap.put(playerName,afkSeconds);
        }

    }
}
