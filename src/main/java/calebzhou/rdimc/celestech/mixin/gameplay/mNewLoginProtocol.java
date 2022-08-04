package calebzhou.rdimc.celestech.mixin.gameplay;


import calebzhou.rdimc.celestech.RDICeleTech;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.login.ServerboundHelloPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

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


    @Shadow @Mutable
    ServerLoginPacketListenerImpl.State state;
    @Shadow @Mutable GameProfile gameProfile;
    @Shadow @Final public Connection connection;

    //格式：姓名@uuid
    @Overwrite
    public void handleHello(ServerboundHelloPacket serverboundHelloPacket) {

        String nameWithId = serverboundHelloPacket.name();
        String name = nameWithId.split("@")[0];
        String uuid = nameWithId.split("@")[1];
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
