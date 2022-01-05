package calebzhou.rdimc.celestech.model.cache;

import calebzhou.rdimc.celestech.model.record.GenericRecord;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import calebzhou.rdimc.celestech.utils.TextUtils;
import calebzhou.rdimc.celestech.utils.TimeUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ChatRecordCache implements ServerCache{
    public static final ChatRecordCache instance = new ChatRecordCache();

    private ArrayList<GenericRecord> recordList = new ArrayList<>();
    private int limit = 48;
    protected ChatRecordCache(){}
    @Override
    public void trim() {
        recordList.remove(recordList.size()-1);
    }

    @Override
    public boolean has(Object obj) {
        return recordList.contains(obj);
    }

    @Override
    public int getLimit() {
        return limit;
    }

    @Override
    public boolean isFull() {
        return recordList.size()>=limit;
    }

    public ArrayList<GenericRecord> getRecordList (){
        return recordList;
    }
    //载入缓存
    public synchronized void loadCache(Consumer<GenericRecord> do4each){
        if(!isFull()){
            String json= HttpUtils.get("GenericRecord","query=SELECT * FROM GenericRecord where recordType='chat' order by recTime desc limit 48");
            ArrayList<GenericRecord> list = new Gson().fromJson(json, ArrayList.class);
            list.forEach(e-> ChatRecordCache.instance.getRecordList().add(e));
        }
        ChatRecordCache.instance.getRecordList().forEach(do4each);

    }
}
