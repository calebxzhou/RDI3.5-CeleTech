package calebzhou.rdimc.celestech;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

import static calebzhou.rdimc.celestech.RDICeleTech.MOD_ID;

public class Networking {
    public static final Identifier NETWORK_COMMAND_STATUS=new Identifier(MOD_ID,"command_status");

    public Networking() {
        registerNetwork();
    }

    private void registerNetwork() {
        ServerPlayNetworking.registerGlobalReceiver(NETWORK_COMMAND_STATUS,((server, player, handler, buf, responseSender) -> {

        }));
    }
}
