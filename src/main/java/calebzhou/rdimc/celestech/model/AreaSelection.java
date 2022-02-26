package calebzhou.rdimc.celestech.model;

import calebzhou.rdimc.celestech.command.AreaException;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import it.unimi.dsi.fastutil.Hash;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class AreaSelection {
    public static final HashMap<String, AreaSelection> map = new HashMap<>();

    public static Vec3i[] getPlayerSelectedArea(String pid){
        AreaSelection pos = AreaSelection.map.get(pid);
        if(!pos.isTwoPositionSelected())
            throw new AreaException("您必须选择两个区域点，才能使用本指令");
        Vec3i xyz1= pos.getPos1();
        Vec3i xyz2= pos.getPos2();
        return new Vec3i[]{xyz1,xyz2};
    }



    private BlockPos pos1;
    private BlockPos pos2;

    public AreaSelection() {
    }

    public AreaSelection(BlockPos pos1, BlockPos pos2) {
        this.pos1 = pos1;
        this.pos2 = pos2;
    }
    public boolean isTwoPositionSelected(){
        return pos1!=null && pos2!=null;
    }
    public BlockPos getPos1() {
        return pos1;
    }

    public void setPos1(BlockPos pos1) {
        this.pos1 = pos1;
    }

    public BlockPos getPos2() {
        return pos2;
    }

    public void setPos2(BlockPos pos2) {
        this.pos2 = pos2;
    }


}
