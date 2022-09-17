package calebzhou.rdi.celestech.model;

import org.apache.commons.lang3.RandomUtils;

import java.io.Serializable;

public class RollPrize implements Serializable {
    public enum Type{
        item,
        creature,
        exp
    }
    String id;
    String type;
    String descr;
    //概率 0~1
    float proba;
    int count;

    //成功抽到奖
    public boolean getPrizeSuccessful(){
        return RandomUtils.nextInt(0,1000)< getProba()*1000;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Type getType() {
        return Type.valueOf(type);
    }

    public void setType(Type type) {
        this.type = type.toString();
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public float getProba() {
        return proba;
    }

    public void setProba(float proba) {
        this.proba = proba;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
