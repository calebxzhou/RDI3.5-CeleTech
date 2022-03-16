package calebzhou.rdimc.celestech.mixin.server;

import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

//节约服务器带宽
@Mixin(MinecraftServer.class)
public class MixinSaveBandwidth {
    //不构建同步时间数据包
    /*@Redirect(method = "Lnet/minecraft/server/MinecraftServer;tickWorlds(Ljava/util/function/BooleanSupplier;)V",
    at=@At(value = "NEW",target = "net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket"))
    private WorldTimeUpdateS2CPacket noNewPacketSyncWorldTime(long time, long timeOfDay, boolean doDaylightCycle){
        return null;
    }*/
    //不同步时间
    @Redirect(method = "Lnet/minecraft/server/MinecraftServer;tickWorlds(Ljava/util/function/BooleanSupplier;)V",
    at=@At(value = "INVOKE",target = "Lnet/minecraft/server/PlayerManager;sendToDimension(Lnet/minecraft/network/Packet;Lnet/minecraft/util/registry/RegistryKey;)V"))
    private void noSyncWorldTime(PlayerList instance, Packet<?> packet, ResourceKey<Level> dimension){

    }
    //不显示玩家延迟
    @Redirect(method = "Lnet/minecraft/server/MinecraftServer;tickWorlds(Ljava/util/function/BooleanSupplier;)V",
            at=@At(value = "INVOKE",target = "Lnet/minecraft/server/PlayerManager;updatePlayerLatency()V"))
    private void noUpdatePlayerPing(PlayerList instance){

    }


}
