package calebzhou.rdimc.celestech.mixin.player;


import calebzhou.rdimc.celestech.RDICeleTech;
import calebzhou.rdimc.celestech.module.Auth;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import calebzhou.rdimc.celestech.utils.IpUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import net.minecraft.DefaultUncaughtExceptionHandler;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.login.ClientboundHelloPacket;
import net.minecraft.network.protocol.login.ServerboundHelloPacket;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;
import org.apache.commons.lang3.Validate;
import org.checkerframework.checker.units.qual.A;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.net.InetSocketAddress;

//@Mixin(ServerConfigHandler.class)
@Mixin(ServerLoginPacketListenerImpl.class)
public abstract class MixinAllowOfflineMode{

    @Shadow private ServerLoginPacketListenerImpl.State state;
    @Shadow private @Nullable GameProfile gameProfile;
    @Shadow @Final public Connection connection;

    /*@Inject(method = "Lnet/minecraft/server/network/ServerLoginPacketListenerImpl;handleHello(Lnet/minecraft/network/protocol/login/ServerboundHelloPacket;)V",
    at = @At("TAIL"))
    private void checkAllowLogin(ServerboundHelloPacket serverboundHelloPacket, CallbackInfo ci){
        Auth.handleHello((ServerLoginPacketListenerImpl)(Object)this,serverboundHelloPacket);
    }*/


   /* @Overwrite   3.5再用！
    public void handleHello(ServerboundHelloPacket serverboundHelloPacket) {
        Validate.validState(this.state == ServerLoginPacketListenerImpl.State.HELLO, "Unexpected hello packet", new Object[0]);
        Auth.handleHello((ServerLoginPacketListenerImpl)(Object)this,serverboundHelloPacket);
        this.gameProfile = new GameProfile(Auth.getNewUuid(gameProfile.getName()),gameProfile.getName());
        this.state = ServerLoginPacketListenerImpl.State.READY_TO_ACCEPT;
    }*/

    @Redirect(
            method = "Lnet/minecraft/server/network/ServerLoginPacketListenerImpl;handleHello(Lnet/minecraft/network/protocol/login/ServerboundHelloPacket;)V",
            at=@At(
                    target = "Lnet/minecraft/network/Connection;send(Lnet/minecraft/network/protocol/Packet;)V",
                    value = "INVOKE"
            )
    )
    private void asd213(Connection instance, Packet<?> packet){
        System.out.println("Hello!");
        try{
            String json1 = HttpUtils.sendRequestPublic("GET", "https://api.mojang.com/users/profiles/minecraft/"
                    + gameProfile.getName());
            System.out.println(json1);
            JsonObject rootObj = JsonParser.parseString(json1).getAsJsonObject();
            String id = rootObj.get("id").getAsString();
            //成功获取了id就是正版玩家，进入正版验证
            RDICeleTech.LOGGER.info("此人是正版 "+id);
            connection.send(packet);
        }catch (IllegalStateException|NullPointerException e){
            System.out.println(gameProfile.getName()+"不是正版");
            gameProfile = createFakeProfile(gameProfile);
            this.state=ServerLoginPacketListenerImpl.State.READY_TO_ACCEPT;
            RDICeleTech.offlineModePlayerList.add(gameProfile.getName());
        }catch (Exception e){
            e.printStackTrace();
        }



    }
    @Redirect(
            method = "Lnet/minecraft/server/network/ServerLoginPacketListenerImpl;handleKey(Lnet/minecraft/network/protocol/login/ServerboundKeyPacket;)V",
            at=@At(
                    target = "Ljava/lang/Thread;start()V",
                    value = "INVOKE"
            )
    )
    private void asd(Thread instance){
        System.out.println("开始正版验证!");
        Thread thread= new Thread(() -> {
                String json1 = HttpUtils.sendRequestPublic("GET", "https://api.mojang.com/users/profiles/minecraft/" + gameProfile.getName());
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
        });
        thread.setUncaughtExceptionHandler(new DefaultUncaughtExceptionHandler(RDICeleTech.LOGGER));
        thread.start();
    }
    @Shadow
    protected abstract GameProfile createFakeProfile(GameProfile profile);

}
