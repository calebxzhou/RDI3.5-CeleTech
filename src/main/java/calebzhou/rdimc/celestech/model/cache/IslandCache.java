package calebzhou.rdimc.celestech.model.cache;

import calebzhou.rdimc.celestech.model.Island;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;

public class IslandCache implements ServerCache {
    public static final IslandCache instance = new IslandCache();
    private HashMap<String, Island> map = new HashMap<>();
    private int limit=512;

    @Override
    public void trim() {
        map.clear();
    }

    @Override
    public boolean has(Object obj) {
        return map.containsKey(obj);
    }

    @Override
    public int getLimit() {
        return limit;
    }

    @Override
    public boolean isFull() {
        return map.size()>=limit;
    }

    public HashMap<String, Island> getMap(){
        return map;
    }
}
