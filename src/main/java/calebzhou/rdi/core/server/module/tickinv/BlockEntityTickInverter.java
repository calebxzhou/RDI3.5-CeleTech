package calebzhou.rdi.core.server.module.tickinv;

import calebzhou.rdi.core.server.ServerLaggingStatus;
import calebzhou.rdi.core.server.utils.ServerUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.status.ServerStatus;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.TickingBlockEntity;

public class BlockEntityTickInverter  {
    public static void handleTick(Level level, TickingBlockEntity ticker){
        try {
			if(!ServerLaggingStatus.isServerVeryLagging()){
				ticker.tick();
			}else{
				level.addBlockEntityTicker(ticker);
			}

        } catch (Exception e) {
            ServerUtils.broadcastChatMessage("TickBlockEntity错误:%s。原因：%s。方块位置：%s。方块类型:%s"
                    .formatted(
                            e.getMessage(),
                            e.getCause(),
                            ticker.getPos(),
                            ticker.getType()));
        }
    }


    private BlockEntityTickInverter(){}
}
//如果已经有了
            /*if(delayTickList.containsKey(bpos)){
                return;
            }*/


//如果服务器延迟高于BAD
            /*if(ServerLaggingStatus.flag>= ServerLaggingStatus.WORST){
                delayTickList.put(bpos,invoker);
            }*/
 /*  public int getDelayTickListSize(){
        return delayTickList.size();
    }
    public void releaseDelayTickList(){
        if(delayTickList.size()==0)
            return;
        BlockPos blockPos = delayTickList.firstKey();
        if(ServerLaggingStatus.flag< ServerLaggingStatus.WORST){
            delayTickList.get(blockPos).tick();
            delayTickList.removeFirst();
        }
    }*/
