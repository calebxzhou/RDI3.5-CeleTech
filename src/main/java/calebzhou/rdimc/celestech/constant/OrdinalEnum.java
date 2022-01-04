package calebzhou.rdimc.celestech.constant;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public interface OrdinalEnum{
    public int getId();

    static <E extends Enum<E>> Map<Integer, E> getValues(Class<E> clzz){
        Map<Integer, E> m = new HashMap<>();
        for(Enum<E> e : EnumSet.allOf(clzz))
            m.put(((OrdinalEnum)e).getId(), (E)e);

        return m;
    }
}