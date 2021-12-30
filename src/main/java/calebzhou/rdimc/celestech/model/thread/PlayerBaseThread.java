package calebzhou.rdimc.celestech.model.thread;

import calebzhou.rdimc.celestech.utils.PlayerUtils;
import calebzhou.rdimc.celestech.utils.ServerUtils;
import calebzhou.rdimc.celestech.utils.ThreadPool;
import net.minecraft.server.network.ServerPlayerEntity;

public abstract class PlayerBaseThread extends Thread{
    protected ServerPlayerEntity player;
    protected String playerName;
    public PlayerBaseThread(String playerName){
        this.playerName=playerName;
        this.player= PlayerUtils.getPlayerByName(playerName);
    }
    @Override
    public void run() {
        try {
            Thread.sleep(1000);
            //玩家下线则终止线程
            if(!ThreadPool.isPlayerThreadStarted(playerName) || player == null){
                this.interrupt();
                return;
            }
            refreshPlayer();
            execute();
        } catch (InterruptedException e) {
            e.printStackTrace();

        } catch (NullPointerException e){
            //获取不到玩家
            this.interrupt();
            return;
        }
    }
    protected abstract void execute() throws InterruptedException;
    protected void refreshPlayer(){
        this.player = PlayerUtils.getPlayerByName(playerName);
    }
}
