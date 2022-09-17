package calebzhou.rdi.celestech.utils;

import static calebzhou.rdi.celestech.RDICeleTech.MODID;

import net.minecraft.resources.ResourceLocation;

public class IdentifierUtils {
    public static ResourceLocation byClass(Class clazz){
        String name = clazz.getSimpleName().toLowerCase();
        return new ResourceLocation(MODID,name);
    }
}
