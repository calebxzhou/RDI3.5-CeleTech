package calebzhou.rdimc.celestech.module.island;

import calebzhou.rdimc.celestech.model.Island;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;

public class IslandInfoS2CPacket implements Packet<ClientPlayPacketListener> {
    private final Island island;

    public IslandInfoS2CPacket(Island island) {
        this.island = island;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeString(island.toString());
    }

    @Override
    public void apply(ClientPlayPacketListener listener) {

    }

}
