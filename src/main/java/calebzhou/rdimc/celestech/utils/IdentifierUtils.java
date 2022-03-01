package calebzhou.rdimc.celestech.utils;

import net.minecraft.util.Identifier;

import static calebzhou.rdimc.celestech.RDICeleTech.MODID;

public class IdentifierUtils {
    public static Identifier byClass(Class clazz){
        String name = clazz.getSimpleName().toLowerCase();
        return new Identifier(MODID,name);
    }
}
