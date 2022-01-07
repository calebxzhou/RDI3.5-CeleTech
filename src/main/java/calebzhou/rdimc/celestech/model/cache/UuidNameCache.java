package calebzhou.rdimc.celestech.model.cache;

import calebzhou.rdimc.celestech.model.record.UuidNameRecord;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import calebzhou.rdimc.celestech.utils.JsonUtils;
import calebzhou.rdimc.celestech.utils.ThreadPool;
import org.apache.commons.lang3.ThreadUtils;

import java.util.HashMap;
import java.util.List;

public class UuidNameCache implements ServerCache{
    public static final UuidNameCache instance = new UuidNameCache();
    private final HashMap<String,String> map = new HashMap<>();
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

    public HashMap<String, String> getMap() {
        return map;
    }

    @Override
    public void loadCache() {
        ThreadPool.newThread(()->{
            String json = HttpUtils.post("UuidNameRecord", "query=1");
            List<UuidNameRecord> records = JsonUtils.stringToArray(json, UuidNameRecord[].class);
            records.forEach(record -> {
                map.put(record.getPid(),record.getPname());
            });
        });
    }
}
