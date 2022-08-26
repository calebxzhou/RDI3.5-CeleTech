package calebzhou.rdimc.celestech.constant;

import java.io.File;

public class FileConst {
    public static File getMainFolder(){
        return new File( "cele3");
    }
    public static File getPasswordFolder(){
        return new File(getMainFolder(), "password");
    }
    public static File getHwSpecFolder(){
        return new File(getMainFolder(), "hwspec");
    }


}
