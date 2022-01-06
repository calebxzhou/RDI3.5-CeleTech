package calebzhou.rdimc.celestech.constant;

import java.util.Arrays;
import java.util.Map;

public enum ChatRange implements OrdinalEnum{
    all(0,"全"),
    team(1,ColorConstants.DARK_BLUE+"岛"+ColorConstants.RESET),
    mute(2,"无")
    ;

    private int id;
    private String desp;
    ChatRange(int i,String desp) {
        id=i;
        this.desp=desp;
    }
    public int getId(){
        return id;
    }

    public String getDesp() {
        return desp;
    }

    private static Map<Integer,ChatRange> enumap = OrdinalEnum.getValues(ChatRange.class);
    public static ChatRange fromId(int id){
        return enumap.get(id);
    }

}
