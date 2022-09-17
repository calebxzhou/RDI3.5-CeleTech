package calebzhou.rdi.celestech.module;

import calebzhou.rdi.celestech.utils.HttpUtils;
import calebzhou.rdi.celestech.utils.IpUtils;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.login.ServerLoginPacketListener;
import net.minecraft.network.protocol.login.ServerboundHelloPacket;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;

import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.util.UUID;

public class Auth {
    public static void handleHello(ServerLoginPacketListenerImpl listener, ServerboundHelloPacket packet) {
        String usr = packet.getGameProfile().getName();
        String qq = HttpUtils.sendRequestRaw("GET","account_utils","method=rdi2qq","param="+usr);
        byte[] ip = ((InetSocketAddress)listener.getConnection().getRemoteAddress()).getAddress().getAddress();
        String ip10 = ((InetSocketAddress)listener.getConnection().getRemoteAddress()).getHostString();
        String response = HttpUtils.sendRequestRaw("GET", "account_login", "usr=" + usr,"ip="+IpUtils.ip2addr(ip),"ip10="+ip10);
        //登录不成功
        if(!response.equals("true")){
            String reason = "请您登录QQ "+qq.charAt(0)+"********"+qq.charAt(qq.length()-1)+"，向RDI机器人发送： r#login="+ IpUtils.ip2addr(ip)+
                    "\n发送成功后，重新连接服务器。";
            listener.disconnect(Component.literal(reason));
        }
    }
}
