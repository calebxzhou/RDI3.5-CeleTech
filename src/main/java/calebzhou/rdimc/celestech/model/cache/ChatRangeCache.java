package calebzhou.rdimc.celestech.model.cache;

import calebzhou.rdimc.celestech.model.ChatStatus;

import java.util.HashMap;

public class ChatRangeCache implements ServerCache{
    public static final ChatRangeCache instance = new ChatRangeCache();
    private HashMap<String, ChatStatus> map =new HashMap<>();


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

    public HashMap<String,ChatStatus> getMap() {
        return map;
    }
}
