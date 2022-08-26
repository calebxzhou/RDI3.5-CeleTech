package calebzhou.rdimc.celestech;

import net.minecraft.resources.ResourceLocation;

import static calebzhou.rdimc.celestech.RdiSharedConstants.MOD_ID;


//网络包
public class NetworkPackets {
    //C2S 客户端->服务端
    //隔空跳跃
    public static final ResourceLocation LEAP=new ResourceLocation(MOD_ID,"leap");
    //挂机检测
    public static final ResourceLocation AFK_DETECT = new ResourceLocation(MOD_ID,"afk_detect");
    //跳舞树
    public static final ResourceLocation DANCE_TREE_GROW =new ResourceLocation(MOD_ID,"dance_tree_grow");
    //硬件信息
    public static final ResourceLocation HW_SPEC=new ResourceLocation(MOD_ID,"hw_spec");
    //右下角消息弹框
    public static final ResourceLocation POPUP=new ResourceLocation(MOD_ID,"popup");
    //对话框信息
    public static final ResourceLocation DIALOG_INFO = new ResourceLocation(MOD_ID,"dialog_info");
    public static final ResourceLocation SAVE_WORLD = new ResourceLocation(MOD_ID,"save_world");
    //快速繁殖
    public static final ResourceLocation ANIMAL_SEX=new ResourceLocation(MOD_ID,"animal_sex");
    //S2C 服务端->客户端

    //岛屿信息
    public static final ResourceLocation ISLAND_INFO =new ResourceLocation(MOD_ID,"island_info");
}
