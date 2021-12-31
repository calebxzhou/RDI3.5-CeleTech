package calebzhou.rdimc.celestech.model.record;

import java.io.Serializable;

public class UuidNameRecord implements Serializable {
    String pid;
    String pname;

    public UuidNameRecord(String pid, String pname) {
        this.pid = pid;
        this.pname = pname;
    }
}
