package calebzhou.rdimc.celestech.model;

import calebzhou.rdimc.celestech.constant.FileConst;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import net.minecraft.util.math.BlockBox;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

public class VirtualStructure implements Serializable {
    public static HashMap<String, List<VirtualStructure>> STRUCTURE_MAP = new HashMap<>();
    public static final File STRUCTURE_FILE = new File(FileConst.FOLDER,"structure.json");
    String pname;
    Type type;
    BorderedBox range;

    public VirtualStructure() {
    }

    public VirtualStructure(String pname, Type type, BorderedBox range) {
        this.pname = pname;
        this.type = type;
        this.range = range;
    }


    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public BorderedBox getRange() {
        return range;
    }

    public void setRange(BorderedBox range) {
        this.range = range;
    }

    public static VirtualStructure fromString(String json){
        return new Gson().fromJson(json,VirtualStructure.class);
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }


    public enum Type implements Serializable{
        swamp_hut,
        mansion,
        monument;

        @Override
        public String toString() {
            return super.toString();
        }
    }


}
