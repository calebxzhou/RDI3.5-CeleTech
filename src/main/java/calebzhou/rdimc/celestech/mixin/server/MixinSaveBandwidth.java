package calebzhou.rdimc.celestech.mixin.server;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

//节约服务器带宽
@Mixin(MinecraftServer.class)
public class MixinSaveBandwidth {
    //不构建同步时间数据包
    /*@Redirect(method = "Lnet/minecraft/server/MinecraftServer;tickChildren(Ljava/util/function/BooleanSupplier;)V",
    at=@At(value = "NEW",target = "net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket"))
    private WorldTimeUpdateS2CPacket noNewPacketSyncWorldTime(long time, long timeOfDay, boolean doDaylightCycle){
        return null;
    }*/
    //减慢同步时间，原来是20tick一次，改成2000tick一次
    /*@ModifyConstant(method = "tickChildren(Ljava/util/function/BooleanSupplier;)V",
    constant = @Constant(intValue = 20))
    private int noSyncWorldTime(int constant){
        return 2000;
    }*/
    //不更新玩家延迟
    @Redirect(method = "tickChildren(Ljava/util/function/BooleanSupplier;)V",
            at=@At(value = "INVOKE",target = "Lnet/minecraft/server/players/PlayerList;tick()V"))
    private void noUpdatePlayerPing(PlayerList instance){

    }


}
