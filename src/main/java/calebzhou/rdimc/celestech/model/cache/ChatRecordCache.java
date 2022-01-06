package calebzhou.rdimc.celestech.model.cache;

import calebzhou.rdimc.celestech.model.record.GenericRecord;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import calebzhou.rdimc.celestech.utils.JsonUtils;
import calebzhou.rdimc.celestech.utils.TextUtils;
import calebzhou.rdimc.celestech.utils.TimeUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ChatRecordCache implements ServerCache{
    public static final ChatRecordCache instance = new ChatRecordCache();

    private ArrayList<GenericRecord> recordList = new ArrayList<>();
    private int limit = 100;
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
            String json= HttpUtils.post("GenericRecord","query=SELECT @AST FROM GenericRecord where recordType='chat' order by recTime desc limit 48");
            List<GenericRecord> list = JsonUtils.stringToArray(json,GenericRecord[].class);
            Collections.reverse(list);
            list.forEach( e->ChatRecordCache.instance.getRecordList().add(e));
        }
        recordList.forEach(do4each);

    }

}
