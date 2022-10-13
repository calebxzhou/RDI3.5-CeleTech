package calebzhou.rdi.core.server;

import calebzhou.rdi.core.server.model.RdiPlayerLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;

//常量
public class RdiSharedConstants {

	public static final String PROTOCOL_NAME ="RDI Core 3.7";
    //调试模式
    public static final boolean DEBUG = true;
    //modid
    public static final String MOD_ID ="rdict3";
    //空岛存档维度id前缀
    public static final String ISLAND_DIMENSION_PREFIX ="i_";
    public static final String ISLAND_DIMENSION_FULL_PREFIX =RdiSharedConstants.MOD_ID+":"+RdiSharedConstants.ISLAND_DIMENSION_PREFIX;
    //版本号
    public static final int PROTOCOL_VERSION =0x37;
	//主城
	public static final BlockPos SPAWN_LOCATION =new BlockPos(0,138,0);

}
