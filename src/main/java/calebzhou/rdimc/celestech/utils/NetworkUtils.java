package calebzhou.rdimc.celestech.utils;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import static calebzhou.rdimc.celestech.RDICeleTech.MOD_ID;

public class NetworkUtils {
    public static final Identifier COMMAND_STATUS =new Identifier(MOD_ID,"command_status");
    public static final Identifier ISLAND_INFO =new Identifier(MOD_ID,"island_info");
    public static final Identifier MOB_SPAWN =new Identifier(MOD_ID,"mob_spawn");
    public static void sendPacketS2C(PlayerEntity player, Identifier packType,String content){
        ServerPlayerEntity sp = (ServerPlayerEntity) player;
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeString(content);
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
