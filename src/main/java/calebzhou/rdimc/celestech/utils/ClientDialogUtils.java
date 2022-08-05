package calebzhou.rdimc.celestech.utils;

import calebzhou.rdimc.celestech.NetworkPackets;
import net.minecraft.server.level.ServerPlayer;

public class ClientDialogUtils {
    public static void sendDialog(ServerPlayer player,String type, String title, String msg){
        NetworkUtils.sendPacketToClient(player, NetworkPackets.DIALOG_INFO,String.format("%s@%s@%s",type,title,msg));
    }
    public static void sendPopup(ServerPlayer player,String type, String title, String msg){
        NetworkUtils.sendPacketToClient(player, NetworkPackets.POPUP,String.format("%s@%s@%s",type,title,msg));
    }
}
