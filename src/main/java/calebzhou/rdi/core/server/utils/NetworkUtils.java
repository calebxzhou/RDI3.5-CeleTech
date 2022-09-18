package calebzhou.rdi.core.server.utils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.quiltmc.qsl.networking.api.PacketByteBufs;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking;

public class NetworkUtils {
    public static void sendPacketToClient(Player player,ResourceLocation packType, Object content){
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
        ServerPlayNetworking.send((ServerPlayer) player,packType,buf);
    }


}
