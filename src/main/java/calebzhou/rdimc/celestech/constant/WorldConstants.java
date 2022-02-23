package calebzhou.rdimc.celestech.constant;

import calebzhou.rdimc.celestech.RDICeleTech;
import calebzhou.rdimc.celestech.model.CoordLocation;
import calebzhou.rdimc.celestech.model.PlayerLocation;
import net.minecraft.server.world.ServerWorld;

public class WorldConstants {
    public static final PlayerLocation SPAWN_LOCA =new PlayerLocation(0,138,0,0,0, RDICeleTech.getServer().getOverworld());

    public static final String DEFAULT_WORLD = "minecraft:overworld";
}
