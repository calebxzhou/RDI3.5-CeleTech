package calebzhou.rdimc.celestech.mixin.gameplay;


import calebzhou.rdimc.celestech.utils.PlayerUtils;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.login.ServerboundHelloPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;
import org.apache.commons.io.FileUtils;
import org.spongepowered.asm.mixin.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Mixin(MinecraftServer.class)
class mAllowOffline1{
    @Overwrite
    public boolean usesAuthentication() {
        return false;
    }
}
//@Mixin(ServerConfigHandler.class)
@Mixin(ServerLoginPacketListenerImpl.class)
public abstract class mNewLoginProtocol {


    @Shadow public abstract void disconnect(Component component);

    @Shadow @Mutable
    ServerLoginPacketListenerImpl.State state;
    @Shadow @Mutable GameProfile gameProfile;
    @Shadow @Final public Connection connection;

    //格式：姓名@uuid
    @Overwrite
    public void handleHello(ServerboundHelloPacket serverboundHelloPacket) {

        String nameIdPasswd = serverboundHelloPacket.name();
        if(!nameIdPasswd.contains("@")){
            disconnect(Component.literal("登录协议错误1，请更新客户端！"));
            return;
        }
        String[] split = nameIdPasswd.split("@");
        if(split.length<2){
            disconnect(Component.literal("登录协议错误2，请更新客户端！"));
            return;
        }
        String name = split[0];
        String uuid = split[1];
        File pwdFile = PlayerUtils.getPasswordFile(uuid);
        //如果存在密码文件，则验证密码
        if(pwdFile.exists()){
            String pwd = split[2];
            String pwdInFile = null;
            try {
                pwdInFile = FileUtils.readFileToString(pwdFile, StandardCharsets.UTF_8);
            } catch (IOException e) {
                disconnect(Component.literal("无法读取密码文件："+e.getMessage()+"，请联系服主"));
                return;
            }
            if(!pwd.equals(pwdInFile)){
                disconnect(Component.literal("错误的密钥，无法解密游戏数据！"));
                return;
            }
        }

        gameProfile = new GameProfile(UUID.fromString(uuid), name);
        state = ServerLoginPacketListenerImpl.State.READY_TO_ACCEPT;
    }


/*
    @Redirect(method = "handleHello",
            at=@At(target = "Lnet/minecraft/network/Connection;send(Lnet/minecraft/network/protocol/Packet;)V",
                    value = "INVOKE"))
    private void asd213(Connection instance, Packet<?> packet){
        Thread thread= new Thread(() -> {
            RDICeleTech.LOGGER.info("开始验证handleHello");
            try{

                String json1 = HttpUtils.sendRequestFullUrl("GET", "https://api.mojang.com/users/profiles/minecraft/"
                        + gameProfile.getName());
                RDICeleTech.LOGGER.info(json1);
                JsonObject rootObj = JsonParser.parseString(json1).getAsJsonObject();
                String id = rootObj.get("id").getAsString();
                //成功获取了id就是正版玩家，进入正版验证
                RDICeleTech.LOGGER.info("此人是正版 ,UUID="+id);
                connection.send(packet);
            }catch (IllegalStateException|NullPointerException e){
                RDICeleTech.LOGGER.info(gameProfile.getName()+"不是正版，创建fake profile");
                gameProfile = createFakeProfile(gameProfile);
                this.state = ServerLoginPacketListenerImpl.State.READY_TO_ACCEPT;
            }catch (Exception e){
                e.printStackTrace();
            }

        });
        thread.start();



    }
    @Redirect(
            method = "handleKey(Lnet/minecraft/network/protocol/login/ServerboundKeyPacket;)V",
            at=@At(
                    target = "Ljava/lang/Thread;start()V",
                    value = "INVOKE"
            )
    )
    private void asd(Thread instance){
        RDICeleTech.LOGGER.info("开始正版验证handleKey!");
        Thread thread= new Thread(() -> {
            try {
                String json1 = HttpUtils.sendRequestFullUrl("GET", "https://api.mojang.com/users/profiles/minecraft/" + gameProfile.getName());
                JsonObject rootObj = JsonParser.parseString(json1).getAsJsonObject();
                String id = rootObj.get("id").getAsString();
                //成功获取了id就是正版玩家，进入正版验证
                if(id!=null){
                    instance.start();
                }else{
                    //否则进入非正版验证
                    gameProfile = createFakeProfile(gameProfile);
                    state = ServerLoginPacketListenerImpl.State.READY_TO_ACCEPT;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }*/
    @Shadow
    protected abstract GameProfile createFakeProfile(GameProfile profile);

}
