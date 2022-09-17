package calebzhou.rdi.core.server;

import net.minecraft.core.BlockPos;

//常量
public class RdiSharedConstants {


    //调试模式
    public static final boolean DEBUG = true;
    //modid
    public static final String MOD_ID ="rdict3";
    //空岛存档维度id前缀
    public static final String ISLAND_DIMENSION_PREFIX ="i_";
    public static final String ISLAND_DIMENSION_FULL_PREFIX =RdiSharedConstants.MOD_ID+":"+RdiSharedConstants.ISLAND_DIMENSION_PREFIX;
    //版本号
    public static final int VERSION =0x35A;
	//主城
	public static final BlockPos SPAWN_LOCATION =new BlockPos(0,138,0);
	//初始点
	public static final BlockPos INIT_LOCATION =new BlockPos(0,-60,0);
    public static final String[] COLD_STORIES_AUTHOR = new String[]{
            "pop75189",
            "ryannZ"
    };

    public static final String[] COLD_STORIES = new String[]{
      "跑动/下蹲/跳跃可以让树苗快速生长",
      "dav的五菱宏光S开了1145191公里也没舍得换车",
            "你服始于dav中考完的那个夏天(2013.7)",
            "你服的cpu是高通msm8260，双核1.7G主频，dav魔改了mc才让服务器跑起来",
            "你服内存只有1G，还是lpddr2，频率只有266m，dav做了不少骚操作才让服务器跑起来",
            "dav喜欢骑自行车，去见客户三四十公里也会骑他拼夕夕买的山寨捷安特XTC888",
            "dav喜欢小米手机，买过的8台智能机全都是小米",
            "dav在东北读了高中和大学，也许他算半个东北人（？",
    };
}
