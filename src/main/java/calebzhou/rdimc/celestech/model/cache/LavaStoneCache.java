package calebzhou.rdimc.celestech.model.cache;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;

public class LavaStoneCache implements ServerCache {
    public static final LavaStoneCache instance = new LavaStoneCache();
    private HashMap<BlockPos, BlockState> map = new HashMap<>();
    private int limit=512;

    @Override
    public void trim() {
        map.clear();
    }

    @Override
    public boolean has(Object obj) {
        if(obj instanceof BlockPos)
            return map.containsKey(obj);
        else
            return false;
    }

    @Override
    public int getLimit() {
        return limit;
    }

    @Override
    public boolean isFull() {
        return map.size()>=limit;
    }

    @Override
    public void loadCache() {

    }

    public HashMap<BlockPos,BlockState> getMap(){
        return map;
    }
}
