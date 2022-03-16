package calebzhou.rdimc.celestech.utils;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import static calebzhou.rdimc.celestech.RDICeleTech.MODID;

public class NetworkUtils {
    public static final ResourceLocation COMMAND_STATUS =new ResourceLocation(MODID,"command_status");
    public static final ResourceLocation ISLAND_INFO =new ResourceLocation(MODID,"island_info");
    public static final ResourceLocation MOB_SPAWN =new ResourceLocation(MODID,"mob_spawn");
    public static void sendPacketS2C(Player player, ResourceLocation packType,String content){
        ServerPlayer sp = (ServerPlayer) player;
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeUtf(content);
        ServerPlayNetworking.send(sp,packType,buf);
    }

    public NetworkUtils() {
        registerNetwork();
    }

    private void registerNetwork() {
        ServerPlayNetworking.registerGlobalReceiver(COMMAND_STATUS,((server, player, handler, buf, responseSender) -> {

        }));
    }

}
