package calebzhou.rdimc.celestech.module.tickinv;

import calebzhou.rdimc.celestech.utils.ServerUtils;
import calebzhou.rdimc.celestech.utils.WorldUtils;
import net.minecraft.server.level.ServerLevel;

public class WorldTickThread extends Thread{
    private final ServerLevel level;
    private boolean needTickNow;

    public WorldTickThread(ServerLevel level){
        super("RDITickThread_"+WorldUtils.getDimensionName(level));
        this.level=level;
    }

    //轮询如果需要tick，那就tick
    @Override
    public void run() {
        while (true){
            if(needTickNow){
                try {
                    level.tick(()->needTickNow);
                } catch (Exception e) {
                    e.printStackTrace();
                    ServerUtils.broadcastChatMessage("tick world错误"+e+e.getCause());
                }
                needTickNow=false;
            }
        }
    }

    public boolean isNeedTickNow() {
        return needTickNow;
    }

    public void tickNow() {
        this.needTickNow = true;
    }
}
