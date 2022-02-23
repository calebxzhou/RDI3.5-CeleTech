package calebzhou.rdimc.celestech.model;

import calebzhou.rdimc.celestech.command.AreaException;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class AreaSelection {

    public static final Multimap<String, BlockPos> map = LinkedHashMultimap.create(1024,2);

    public static Vec3i[] getPlayerSelectedArea(String pid){
        Collection<BlockPos> posList = AreaSelection.map.get(pid);
        if(posList.size()<2)
            throw new AreaException("您必须选择两个区域点，才能使用本指令");
        BlockPos[] posArr = posList.toArray(new BlockPos[]{});
        Vec3i xyz1= posArr[0];
        Vec3i xyz2= posArr[1];
        return new Vec3i[]{xyz1,xyz2};
    }
}
