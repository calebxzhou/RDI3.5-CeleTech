package calebzhou.rdimc.celestech.mixin.gameplay;


import calebzhou.rdimc.celestech.RDICeleTech;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

//@Mixin(ServerConfigHandler.class)
@Mixin(ServerLoginPacketListenerImpl.class)
public abstract class MixinAllowOfflineMode{

    @Shadow
    ServerLoginPacketListenerImpl.State state;
    @Shadow
    @Nullable GameProfile gameProfile;
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

    @Redirect(method = "handleHello(Lnet/minecraft/network/protocol/login/ServerboundHelloPacket;)V",
            at=@At(target = "Lnet/minecraft/network/Connection;send(Lnet/minecraft/network/protocol/Packet;)V",
                    value = "INVOKE"))
    private void asd213(Connection instance, Packet<?> packet){
        RDICeleTech.LOGGER.info("开始验证");
        try{
            String json1 = HttpUtils.sendRequestPublic("GET", "https://api.mojang.com/users/profiles/minecraft/"
                    + gameProfile.getName());
            RDICeleTech.LOGGER.info(json1);
            JsonObject rootObj = JsonParser.parseString(json1).getAsJsonObject();
            String id = rootObj.get("id").getAsString();
            //成功获取了id就是正版玩家，进入正版验证
            RDICeleTech.LOGGER.info("此人是正版 "+id);
            connection.send(packet);
        }catch (IllegalStateException|NullPointerException e){
            RDICeleTech.LOGGER.info(gameProfile.getName()+"不是正版，创建fake profile");
            gameProfile = createFakeProfile(gameProfile);
            this.state = ServerLoginPacketListenerImpl.State.READY_TO_ACCEPT;
        }catch (Exception e){
            e.printStackTrace();
        }



    }
    @Redirect(
            method = "handleKey(Lnet/minecraft/network/protocol/login/ServerboundKeyPacket;)V",
            at=@At(
                    target = "Ljava/lang/Thread;start()V",
                    value = "INVOKE"
            )
    )
    private void asd(Thread instance){
        RDICeleTech.LOGGER.info("开始正版验证!");
        Thread thread= new Thread(() -> {
            try {
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }
    @Shadow
    protected abstract GameProfile createFakeProfile(GameProfile profile);

}
