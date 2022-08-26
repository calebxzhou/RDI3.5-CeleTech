package calebzhou.rdimc.celestech.module.tickinv;

import calebzhou.rdimc.celestech.ServerStatus;
import calebzhou.rdimc.celestech.utils.ServerUtils;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.TickingBlockEntity;

import static calebzhou.rdimc.celestech.ServerStatus.BAD;

public class BlockEntityTickInverter implements ITickDelayable{
    public static final BlockEntityTickInverter INSTANCE= new BlockEntityTickInverter();
    private final Object2ObjectLinkedOpenHashMap<BlockPos, TickingBlockEntity> delayTickList = new Object2ObjectLinkedOpenHashMap<>();
    public void tick(TickingBlockEntity invoker){
        try {
            BlockPos bpos = invoker.getPos();
            //如果已经有了
            if(delayTickList.containsKey(bpos)){
                return;
            }
            invoker.tick();

            //如果服务器延迟高于BAD
            if(ServerStatus.flag>=BAD){
                delayTickList.put(bpos,invoker);
            }
        } catch (Exception e) {
            ServerUtils.broadcastChatMessage("TickBlockEntity错误:%s。原因：%s。方块位置：%s。方块类型:%s"
                    .formatted(
                            e.getMessage(),
                            e.getCause(),
                            invoker.getPos(),
                            invoker.getType()));
        }
    }
    public int getDelayTickListSize(){
        return delayTickList.size();
    }
    public void releaseDelayTickList(){
        if(delayTickList.size()==0)
            return;
        BlockPos blockPos = delayTickList.firstKey();
        if(ServerStatus.flag<BAD){
            delayTickList.get(blockPos).tick();
            delayTickList.removeFirst();
        }
    }

    private BlockEntityTickInverter(){}
}
