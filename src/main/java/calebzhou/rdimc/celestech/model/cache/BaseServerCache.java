package calebzhou.rdimc.celestech.model.cache;

import calebzhou.rdimc.celestech.model.Island;
import calebzhou.rdimc.celestech.model.record.GenericRecord;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

public abstract class BaseServerCache<T extends Map,K,V> {
    //岩浆生成的 石头
    public static final BaseServerCache<BlockPos, BlockState> lavaGenStoneMap = new BaseServerCache(HashMap.class,512);

    protected T cacheMap ;
    protected int limit;
    public BaseServerCache(T map, int limit){
        cacheMap = map;
        this.limit = limit;
    }

    public void add(K keyObj,V valueObj){
        if(cacheMap.size()>limit){
            trim();
        }
        cacheMap.put(keyObj, valueObj);
    }

    public abstract void trim();

    public boolean has(K key){
        return cacheMap.containsKey(key);
    }
    public int getLimit(){
        return limit;
    }
    public boolean isFull(){
        return cacheMap.size()>=limit;
    }
    public void forEach(BiConsumer<K,V> funcPntr){
        cacheMap.forEach(funcPntr);
    }
}
