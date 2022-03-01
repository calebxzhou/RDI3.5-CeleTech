package calebzhou.rdimc.celestech.module;

import calebzhou.rdimc.celestech.api.NetworkReceivableC2S;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

import static calebzhou.rdimc.celestech.RDICeleTech.MODID;

public class AfkDetect implements NetworkReceivableC2S {
    public static final Object2IntOpenHashMap<String> afkMap = new Object2IntOpenHashMap<>();
    public static final Identifier NETWORK =new Identifier(MODID,"afk_detect");
    @Override
    public void registerNetworking() {
        ServerPlayNetworking.registerGlobalReceiver(NETWORK,((server, player, handler, buf, responseSender) -> {
            String[] s = buf.readString().split(",");
            String pname = s[0];
            String status = s[1];
            int tick = Integer.parseInt(s[2]);
            if(status.equalsIgnoreCase("afk"))
                afkMap.put(pname,tick);
            else
                afkMap.remove(pname);
        }));
    }
}
