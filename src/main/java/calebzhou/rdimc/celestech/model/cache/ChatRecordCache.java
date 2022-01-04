package calebzhou.rdimc.celestech.model.cache;

import calebzhou.rdimc.celestech.model.record.GenericRecord;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ChatRecordCache implements ServerCache{
    public static final ChatRecordCache instance = new ChatRecordCache();

    private ArrayList<GenericRecord> recordList = new ArrayList<>();
    private int limit = 48;

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
        return recordList.size()==limit;
    }

    public void forEach(Consumer fp) {
        recordList.stream().forEach(fp);
    }
    public ArrayList<GenericRecord> getRecordList (){
        return recordList;
    }
}
