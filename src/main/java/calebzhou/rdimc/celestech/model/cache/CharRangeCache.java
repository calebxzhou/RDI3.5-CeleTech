package calebzhou.rdimc.celestech.model.cache;

import calebzhou.rdimc.celestech.constant.ChatAction;
import calebzhou.rdimc.celestech.constant.ChatRange;
import calebzhou.rdimc.celestech.model.record.GenericRecord;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import java.util.ArrayList;
import java.util.HashMap;

public class CharRangeCache implements ServerCache{
    public static final CharRangeCache instance = new CharRangeCache();
    private Table<String, ChatAction,ChatRange> table = HashBasedTable.create();


    @Override
    public void trim() {
        table.clear();
    }

    @Override
    public boolean has(Object obj) {
        return table.containsRow(obj);
    }

    @Override
    public int getLimit() {
        return limit;
    }

    @Override
    public boolean isFull() {
        return table.size()>=limit;
    }

    public Table<String, ChatAction, ChatRange> getTable() {
        return table;
    }
}
