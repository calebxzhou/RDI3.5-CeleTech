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
    protected void refreshPlayer(){
        this.player = PlayerUtils.getPlayerByName(playerName);
    }

    @Override
    public void run() {
        try {
            while (true){
                Thread.sleep(500);
                refreshPlayer();
                execute();
            }
        } catch (NullPointerException|InterruptedException e){
            //获取不到玩家
            this.interrupt();
        }
    }
    protected abstract void execute() throws InterruptedException;

}
