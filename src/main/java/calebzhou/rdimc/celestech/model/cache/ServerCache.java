package calebzhou.rdimc.celestech.model.cache;


import java.util.function.BiConsumer;

public interface ServerCache {
    int limit = 512;
    //把多余的条目删除掉,可以是list删除最后一个,也可以是map全部清除
    void trim();
    boolean has(Object obj);
    int getLimit();
    boolean isFull();
    void loadCache();
}
