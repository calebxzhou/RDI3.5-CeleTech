package calebzhou.rdi.core.server.module.tickinv;

import calebzhou.rdi.core.server.command.impl.TpsCommand;
import calebzhou.rdi.core.server.misc.ServerLaggingStatus;
import calebzhou.rdi.core.server.misc.TickTaskManager;
import calebzhou.rdi.core.server.utils.ServerUtils;
import calebzhou.rdi.core.server.utils.WorldUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.TickingBlockEntity;

public class BlockEntityTickInverter  {
    public static void handleTick(Level level, TickingBlockEntity ticker){
        try {
			if(!ServerLaggingStatus.isServerVeryLagging()){
				ticker.tick();
			}else{
				/*PlayerUtils.broadcastMessageToLevel((ServerLevel) level,
						Component.literal("跳过BlockEntity ticking:%s %s".formatted(ticker.getPos().toShortString(),ticker.getType()))
								.withStyle(ChatFormatting.GOLD)
								.withStyle(ChatFormatting.ITALIC),
						true);*/
				TickTaskManager.addDelayTickTask(level,()->{
					ticker.tick();
					TpsCommand.delayTickStatus.put(WorldUtils.getDimensionName(level),
							Component.literal("延迟tick容器")
									.append(ticker.getType())
									.append(Component.literal(ticker.getPos().toShortString())));
				});
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
