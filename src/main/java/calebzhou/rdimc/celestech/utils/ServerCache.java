package calebzhou.rdimc.celestech.utils;

import calebzhou.rdimc.celestech.model.record.GenericRecord;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ServerCache<K,V> {
    //岩浆生成的 石头
    public static final ServerCache<BlockPos, BlockState> lavaGenStoneMap = new ServerCache(HashMap.class,512);
    public static final ServerCache<String, GenericRecord> chatRecord = new ServerCache<>(ConcurrentHashMap.class,48);


    private Map<K,V> cacheMap ;
    private int limit;
    public ServerCache(Class<? extends Map> mapType,int limit){
        try {
            this.cacheMap = mapType.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        this.limit = limit;
    }
    public void put(K keyObj,V valueObj){
        if(cacheMap.size()>limit){
            clear();
        }
        cacheMap.put(keyObj, valueObj);
    }
    public void remove(K keyObj){
        cacheMap.remove(keyObj);
    }
    public boolean has(K key){
        return cacheMap.containsKey(key);
    }
    public V get(K key){
        return cacheMap.get(key);
    }
    public void clear(){
        cacheMap.clear();
    }
    public int getLimit(){
        return limit;
    }
    public boolean isFull(){
        return cacheMap.size()>=limit;
    }
    public void forEach(BiConsumer<K,V> cos){
        cacheMap.forEach(cos);
    }
}
