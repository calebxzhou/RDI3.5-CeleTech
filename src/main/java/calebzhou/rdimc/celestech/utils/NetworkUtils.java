package calebzhou.rdimc.celestech.utils;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import static calebzhou.rdimc.celestech.RDICeleTech.MODID;

public class NetworkUtils {
    public static void sendPacketToClient(ServerPlayer player, ResourceLocation packType, Object content){
        FriendlyByteBuf buf = PacketByteBufs.create();
        if(content instanceof Integer i)
            buf.writeInt(i);
        else if(content instanceof Double i)
            buf.writeDouble(i);
        else if(content instanceof Float i)
            buf.writeFloat(i);
        else if(content instanceof Long i)
            buf.writeLong(i);
        else if(content instanceof CompoundTag i)
            buf.writeNbt(i);
        else
            buf.writeUtf(content+"");
        ServerPlayNetworking.send(player,packType,buf);
    }

}
