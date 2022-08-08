package calebzhou.rdimc.celestech.constant;

import calebzhou.rdimc.celestech.RDICeleTech;

import java.io.File;

public class FileConst {
    public static File getMainFolder(){
        return new File(RDICeleTech.getServer().getServerDirectory(), "cele3");
    }
    public static File getPasswordFolder(){
        return new File(getMainFolder(), "password");
    }


}
