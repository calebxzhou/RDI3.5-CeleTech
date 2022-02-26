package calebzhou.rdimc.celestech.mixin.player;


import calebzhou.rdimc.celestech.RDICeleTech;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.mojang.authlib.Agent;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.ProfileLookupCallback;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerConfigHandler;
import net.minecraft.server.network.ServerLoginNetworkHandler;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.StringHelper;
import net.minecraft.util.logging.UncaughtExceptionLogger;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;

import java.util.Collection;
import java.util.UUID;

//@Mixin(ServerConfigHandler.class)
@Mixin(ServerLoginNetworkHandler.class)
public abstract class MixinAllowOfflineMode{
    @Shadow @Final public ClientConnection connection;
    @Shadow @Mutable
    private GameProfile profile;
    @Shadow @Mutable
    private ServerLoginNetworkHandler.State state;
    @Shadow @Final private static Logger LOGGER;

    @Redirect(
            method = "Lnet/minecraft/server/network/ServerLoginNetworkHandler;onHello(Lnet/minecraft/network/packet/c2s/login/LoginHelloC2SPacket;)V",
            at=@At(
                    target = "Lnet/minecraft/network/ClientConnection;send(Lnet/minecraft/network/Packet;)V",
                    value = "INVOKE"
            )
    )
    private void asd213(ClientConnection instance, Packet<?> packet){
        System.out.println("Hello!");
        try{
            String json1 = HttpUtils.sendRequestPublic("GET", "https://api.mojang.com/users/profiles/minecraft/" + profile.getName());
            System.out.println(json1);
            JsonObject rootObj = JsonParser.parseString(json1).getAsJsonObject();
            String id = rootObj.get("id").getAsString();
            //成功获取了id就是正版玩家，进入正版验证
            LOGGER.info("此人是正版 "+id);
            connection.send(packet);
        }catch (IllegalStateException|NullPointerException e){
            System.out.println(profile.getName()+"不是正版");
            profile = toOfflineProfile(profile);
            this.state=ServerLoginNetworkHandler.State.READY_TO_ACCEPT;
            RDICeleTech.offlineModePlayerList.add(profile.getName());
        }catch (Exception e){
            e.printStackTrace();
        }



    }
    @Redirect(
            method = "Lnet/minecraft/server/network/ServerLoginNetworkHandler;onKey(Lnet/minecraft/network/packet/c2s/login/LoginKeyC2SPacket;)V",
            at=@At(
                    target = "Ljava/lang/Thread;start()V",
                    value = "INVOKE"
            )
    )
    private void asd(Thread instance){
        System.out.println("开始正版验证!");
        Thread thread= new Thread(() -> {
                GameProfile gameProfile = profile;
                String json1 = HttpUtils.sendRequestPublic("GET", "https://api.mojang.com/users/profiles/minecraft/" + gameProfile.getName());
                JsonObject rootObj = JsonParser.parseString(json1).getAsJsonObject();
                String id = rootObj.get("id").getAsString();
                //成功获取了id就是正版玩家，进入正版验证
                if(id!=null){
                    instance.start();
                }else{
                    //否则进入非正版验证
                    profile = toOfflineProfile(gameProfile);
                    state = ServerLoginNetworkHandler.State.READY_TO_ACCEPT;
                }
        });
        thread.setUncaughtExceptionHandler(new UncaughtExceptionLogger(LOGGER));
        thread.start();
    }
    @Shadow
    protected abstract GameProfile toOfflineProfile(GameProfile profile);

}