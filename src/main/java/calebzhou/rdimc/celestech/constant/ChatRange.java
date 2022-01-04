package calebzhou.rdimc.celestech.constant;

import java.util.Arrays;
import java.util.Map;

public enum ChatRange implements OrdinalEnum{
    all(0),
    team(1),
    no(2)
    ;

    private int id;
    ChatRange(int i) {
        id=i;
    }
    public int getId(){
        return id;
    }

    private static Map<Integer,ChatRange> enumap = OrdinalEnum.getValues(ChatRange.class);
    public static ChatRange fromId(int id){
        return enumap.get(id);
    }

}
