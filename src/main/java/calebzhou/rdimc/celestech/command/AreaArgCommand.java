package calebzhou.rdimc.celestech.command;

import calebzhou.rdimc.celestech.constant.CoordType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3i;
import org.jetbrains.annotations.NotNull;

public abstract class AreaArgCommand extends BaseCommand implements ArgCommand{
    protected String nameArg;
    protected String[] split;
    public AreaArgCommand(String command, int permissionLevel, boolean isAsync) {
        super(command, permissionLevel, isAsync);
    }
    //解析玩家输入的坐标值
    protected int getCoord(CoordType coordType){
        String str;
        switch (coordType){
            case x1->str=split[0];
            case y1->str=split[1];
            case z1->str=split[2];
            case x2->str=split[3];
            case y2->str=split[4];
            case z2->str=split[5];
            default -> str="";
        }
        return Integer.parseInt(str);
    }
    //适用于x1,y1,z1,x2,y2,z2这样参数的指令，分割成两个向量
    protected Vec3i[] parseToPosition(String arg) throws ArrayIndexOutOfBoundsException {
        String[] split = arg.split(",");
        Vec3i xyz1 = new Vec3i(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
        Vec3i xyz2 = new Vec3i(Integer.parseInt(split[3]), Integer.parseInt(split[4]), Integer.parseInt(split[5]));
        return new Vec3i[]{xyz1, xyz2};
    }
    /**
     * 把坐标值包装成Vec3i向量
     * @param coordType xyz1（点1）或者xyz2（点2），
     */

    protected Vec3i getCoordVec(@NotNull CoordType coordType){
        switch (coordType){
            case xyz1 -> {
                return new Vec3i(getCoord(CoordType.x1),getCoord(CoordType.y1),getCoord(CoordType.z1));
            }
            case xyz2->{
                return new Vec3i(getCoord(CoordType.x2),getCoord(CoordType.y2),getCoord(CoordType.z2));
            }
            default -> {
                throw new IllegalArgumentException();
            }
        }
    }

    public void onExecute(ServerPlayerEntity player, String arg){
        split = arg.split(",");
        nameArg = split[6];
    }

}
