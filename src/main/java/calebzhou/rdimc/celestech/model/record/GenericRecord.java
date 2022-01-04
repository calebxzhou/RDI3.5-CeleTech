package calebzhou.rdimc.celestech.model.record;

import calebzhou.rdimc.celestech.utils.TimeUtils;

import java.io.Serializable;
import java.sql.Timestamp;

public class GenericRecord implements Serializable {
    String pid;
    String recordType;
    String src;
    String target;
    String content;
    Timestamp recTime;
    public GenericRecord(){

    }
    public GenericRecord(String pid, RecordType recordType, String src, String target, String content) {
        this.pid = pid;
        this.recordType = recordType.toString();
        this.src = src;
        this.target = target;
        this.content = content;
        this.recTime = TimeUtils.getNow();
    }

    public String getPid() {
        return pid;
    }

    public String getRecordType() {
        return recordType;
    }

    public String getSrc() {
        return src;
    }

    public String getTarget() {
        return target;
    }

    public String getContent() {
        return content;
    }

    public Timestamp getRecTime() {
        return recTime;
    }
}