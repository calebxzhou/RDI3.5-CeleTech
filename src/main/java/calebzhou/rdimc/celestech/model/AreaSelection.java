package calebzhou.rdimc.celestech.model;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;

public class AreaSelection {

    public static final Multimap<String, BlockPos> map = LinkedHashMultimap.create(1024,2);



    private BlockPos p1;
    private BlockPos p2;

    public AreaSelection(BlockPos p1, BlockPos p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public AreaSelection() {
    }

    public BlockPos getP1() {
        return p1;
    }

    public void setP1(BlockPos p1) {
        this.p1 = p1;
    }

    public BlockPos getP2() {
        return p2;
    }

    public void setP2(BlockPos p2) {
        this.p2 = p2;
    }
}
