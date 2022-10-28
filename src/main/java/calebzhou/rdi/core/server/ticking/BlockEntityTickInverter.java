package calebzhou.rdi.core.server.ticking;

import calebzhou.rdi.core.server.command.impl.TpsCommand;
import calebzhou.rdi.core.server.misc.ServerLaggingStatus;
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
